package hsrmod.monsters.TheEnding;

import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import hsrmod.actions.ForceWaitAction;
import hsrmod.effects.TopWarningEffect;
import hsrmod.misc.VideoManager;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.uniqueDebuffs.DestructionFirstPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AntiCreator extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = AntiCreator.class.getSimpleName();

    int hatredAmt = specialAs ? 44 : 24;
    int thirtyAmt = 6;
    int strengthAmt = moreDamageAs ? 2 : 1;
    int statusCardAmt = 2;
    int maxHpIncAmt;
    int maxHpDecPercent = 24;
    int actionLockAmt = 60;

    boolean useUlt = false;
    boolean exhaustConditionTriggered = false;

    AbstractMonster irontomb;
    AbstractMonster mythos;
    AbstractMonster logos;
    
    List<String> cardsCache = new ArrayList<>();

    public AntiCreator() {
        super(ID, 444, 400, -100, 100);
        bgm = "Agony Converging into River";
        floatIndex = -1;
        maxHpIncAmt = maxHealth;

        // Move 0: 灵的囚笼必遭焚毁⚠
        addMove(Intent.ATTACK_DEBUFF, 10, 5, mi -> {
            addToBot(new VFXAction(new BossCrystalImpactEffect(p.hb.cX, p.hb.cY)));
            
            attack(mi, AbstractGameAction.AttackEffect.FIRE, AttackAnim.FAST);
            ModHelper.addToBotAbstract(() -> {
                int hpLoss = AbstractDungeon.player.maxHealth * maxHpDecPercent / 100;
                AbstractDungeon.player.decreaseMaxHealth(hpLoss);
            });
        });

        // Move 1: 神的冠冕将要粉碎⚠
        addMove(Intent.ATTACK, 10, 2, mi -> {
            addToBot(new VFXAction(new CollectorCurseEffect(p.hb.cX, p.hb.cY)));
            
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
            if (ModHelper.check(mythos) && mythos.intent != Intent.STUN) ModHelper.downgradePile(p.drawPile);
            if (ModHelper.check(logos) && logos.intent != Intent.STUN) ModHelper.downgradePile(p.discardPile);
        });

        // Move 2: 莫要臣服暴政⚠
        addMove(Intent.BUFF, mi -> {
            addToBot(new VFXAction(new HeartMegaDebuffEffect()));
            addToBot(new ForceWaitAction(1f));
            
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthAmt)));
            addToBot(new ApplyPowerAction(p, this, new DescentIntoChaosPower(p, 1)));
            ModHelper.addToBotAbstract(() -> {
                List<AbstractCard> cards = CardLibrary.getAllCards().stream().filter((c) -> c.type == AbstractCard.CardType.STATUS).collect(Collectors.toList());
                if (!cards.isEmpty()) {
                    for (int i = 0; i < statusCardAmt; i++) {
                        if (ModHelper.check(mythos) && mythos.intent != Intent.STUN) {
                            AbstractCard card = GeneralUtil.getRandomElement(cards, AbstractDungeon.cardRandomRng);
                            if (card != null) {
                                addToTop(new MakeTempCardInDrawPileAction(card.makeCopy(), 1, true, true));
                            }
                        }
                        if (ModHelper.check(logos) && logos.intent != Intent.STUN) {
                            AbstractCard card = GeneralUtil.getRandomElement(cards, AbstractDungeon.cardRandomRng);
                            if (card != null) {
                                addToTop(new MakeTempCardInDiscardAction(card.makeCopy(), 1));
                            }
                        }
                    }
                }
            });
        });

        // Move 3: 莫要困毙洞中⚠
        addMove(Intent.ATTACK_DEBUFF, 5, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new ActionLockPower(p, actionLockAmt)));
            addToBot(new ApplyPowerAction(p, this, new DescentIntoChaosPower(p, 1)));
        });

        // Move 4: 去反抗，去毁灭 
        addMove(Intent.DEFEND_BUFF, mi -> {
            addToBot(new GainBlockAction(this, 240));
            addToBot(new ApplyPowerAction(this, this, new LockToughnessPower(this)));
            useUlt = true;
        });

        // Move 5: 烧尽神国，弃绝世界⚠ 
        addMove(Intent.ATTACK, 5, 10, mi -> {
            useUlt = false;
            if (!hasPower(ChargingPower.POWER_ID)) return;

            if (!VideoManager.play("AntiCreator.mp4", 11.0f))
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV("AntiCreator_0.mp4", 3));
            
            // addToBot(new VFXAction(new BorderLongFlashEffect(Color.SCARLET)));
            // addToBot(new VFXAction(new ScreenOnFireEffect()));
            for (int i = 0; i < mi.damageTimeSupplier.get(); i++) {
                addToBot(new VFXAction(new WeightyImpactEffect(p.hb.cX, p.hb.cY, Color.SCARLET)));
                // addToBot(new DamageAction(p, this.damage.get(mi.index), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
            addToBot(new ForceWaitAction(1));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            
            ModHelper.addToBotAbstract(() -> {
                if (ModHelper.check(mythos) && mythos.intent != Intent.STUN) {
                    new ArrayList<>(AbstractDungeon.player.drawPile.group).forEach(c ->
                            addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile)));
                }
                if (ModHelper.check(logos) && logos.intent != Intent.STUN) {
                    new ArrayList<>(AbstractDungeon.player.discardPile.group).forEach(c ->
                            addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile)));
                }
            });
            addToBot(new RemoveSpecificPowerAction(this, this, LockToughnessPower.POWER_ID));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        BaseMod.subscribe(this);
        addToBot(new ApplyPowerAction(this, this, new AmphoreanHatredPower(this, hatredAmt)));
        ModHelper.addToBotAbstract(() -> {
            irontomb = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(Irontomb.ID));
            addToTop(new ApplyPowerAction(this, this, new ThirtyMillionCyclesOfSinPower(this, thirtyAmt, irontomb)));
        });

        addToBot(new ApplyPowerAction(p, this, new AmphoreanLovePower(p, 1)));
        addToBot(new ApplyPowerAction(p, this, new DescentIntoChaosPower(p, 1)));
        addToBot(new ApplyPowerAction(p, this, new DestructionFirstPower(p, 1)));

        logos = new ManipulatedLogos(200, 0);
        addToBot(new SpawnMonsterAction(logos, false, 2));
        logos.usePreBattleAction();
        mythos = new ImprisonedMythos(-400, 0);
        addToBot(new SpawnMonsterAction(mythos, false, 2));
        mythos.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        if (halfDead) {
            increaseMaxHp(maxHpIncAmt, true);
            addToBot(new HealAction(this, this, maxHealth));
            if (moreDamageAs)
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
            halfDead = false;
            addToBot(new SFXAction("VO_AWAKENEDONE_1"));
            addToBot(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05F, true));
        }
        super.takeTurn();
    }

    @Override
    protected void getMove(int i) {
        int result;
        String description = "";

        // If ChargingPower is active, fire the charged final attack next
        if (useUlt) {
            result = 5;
            description = MOVES[11];
            AbstractDungeon.topLevelEffects.add(new TopWarningEffect(DIALOG[1]));
        } else {
            // Interrupt conditions for 去反抗，去毁灭
            int hatredCount = ModHelper.getPowerCount(p, AmphoreanHatredPower.POWER_ID);
            boolean hateCondition = hatredCount > 75;
            boolean turnCondition = turnCount == 4; // fires on the 5th turn
            boolean exhaustCondition = !exhaustConditionTriggered
                    && AbstractDungeon.player.exhaustPile.size() > 24;

            if (!lastTwoMoves((byte) 5) && (hateCondition || turnCondition || exhaustCondition)) {
                if (exhaustCondition) exhaustConditionTriggered = true;
                result = 4;
                description = MOVES[10];
            } else {
                result = turnCount % 4;
                switch (result) {
                    case 0: description = GeneralUtil.tryFormat(MOVES[6], maxHpDecPercent); break;
                    case 1: 
                        description = MOVES[7];
                        AbstractDungeon.topLevelEffects.add(new TopWarningEffect(DIALOG[0]));
                        break;
                    case 2: description = GeneralUtil.tryFormat(MOVES[8], statusCardAmt, statusCardAmt); break;
                    case 3: description = GeneralUtil.tryFormat(MOVES[9], actionLockAmt); break;
                }
            }
        }

        // Normal 4-move rotation
        if (!hasPower(ChargingPower.POWER_ID))
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, description, 1, result != 5)));
        setMove(result);
        turnCount++;
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead && AbstractDungeon.getCurrRoom().cannotLose) {
            this.halfDead = true;
            for (AbstractPower pow : powers) {
                pow.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }

            this.addToTop(new ClearCardQueueAction());
            addToBot(new InstantKillAction(mythos));
            addToBot(new InstantKillAction(logos));
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die(triggerRelics);
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);

            this.onBossVictoryLogic();
        }
    }
    
    List<String> cachedCardIds = new ArrayList<>(Arrays.asList("Aglaea", "Tribbie", "Mydei", "Castorice", "Anaxa", "Hyacine", "Cipher", "Phainon", "Hysilens", "Cerydra", "Evernight", "PermansorTerrae"));

    @Override
    public void receiveCardUsed(AbstractCard card) {
        if (SubscriptionManager.checkSubscriber(this) && card.hasTag(CustomEnums.CHRYSOS_HEIR)) {
            String cardId = card.cardID.substring(0, card.cardID.length() - 2);
            if (!cardsCache.contains(cardId) && cachedCardIds.contains(cardId)) {
                chrysosHeirVoice(cardId);
            }
        }
    }
    
    void chrysosHeirVoice(String id) {
        cardsCache.add(id);
        final String finalVoice = ID + "_" +     id;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(finalVoice, 3));
    }
}
