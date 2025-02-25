package hsrmod.monsters.TheBeyond;

import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import hsrmod.actions.ForceWaitAction;
import hsrmod.actions.LockToughnessAction;
import hsrmod.actions.UnlockToughnessAction;
import hsrmod.cardsV2.Curse.Entangle;
import hsrmod.effects.SkaracabazBallEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Skaracabaz extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = Skaracabaz.class.getSimpleName();

    int defenseCount = 90;
    int summonCount = 2;
    int multiplyCount = 5;
    int insectEggCount = 5;
    boolean canSpawn = false;

    public Skaracabaz() {
        super(ID, 581, 483, -100, 100);
        bgm = "Aberrant Receptacle";
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? -1 : 1;

        defenseCount = specialAs ? 90 : 90;
        summonCount = specialAs ? 2 : 1;
        multiplyCount = specialAs ? 3 : 3;
        insectEggCount = specialAs ? 3 : 1;

        addSlot(-550, 300);
        addSlot(-450, 0);
        addSlot(300, 0);
        addSlot(400, 300);
        monFunc = slot -> {
            BaseMonster result = null;
            if (slots.stream().noneMatch(s -> ModHelper.check(s.monster) && s.monster instanceof LesserSting)
                    && AbstractDungeon.aiRng.random(getEmptySlotCount()) == 0) {
                result = new LesserSting(slot.x, slot.y);
            } else {
                result = new GnawSting(slot.x, slot.y);
            }
            result.rollMove();
            result.createIntent();
            return result;
        };

        addMoveA(Intent.ATTACK_DEBUFF, 1, 9, mi -> {
            // addToBot(new VFXAction(new BloodShotEffect(hb.cX, hb.cY, p.hb.cX, p.hb.cY, 9), Settings.FAST_MODE ? 0.25F : 0.6F));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new EntanglePower(p, this, 1)));
            addToBot(new MakeTempCardInDrawPileAction(new Entangle(), 1, true, true));
            spawnMonsters(1);
        });
        addMove(Intent.BUFF, mi -> {
            spawnMonsters(summonCount);
            addToBot(new ApplyPowerAction(this, this, new DefensePower(this, hasPower(ExtraToughnessPower.POWER_ID) && !moreHPAs ? defenseCount : defenseCount / 3)));
            addToBot(new ApplyPowerAction(this, this, new MultiplyPower(this, multiplyCount)));
            if (insectEggCount > 0)
                addToBot(new ApplyPowerAction(this, this, new InsectEggPower(this, insectEggCount)));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            BaseMod.unsubscribe(this);
            BaseMod.subscribe(this);
        });
        addMoveA(Intent.ATTACK, 3,
                () -> AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> ModHelper.check(m) ? 1 : 0).sum(),
                mi -> {
                    if (hasPower(ChargingPower.POWER_ID)) {
                        addToBot(new VFXAction(new SkaracabazBallEffect(hb.cX, hb.cY + hb.height / 2, p.hb.cX, p.hb.cY), Settings.FAST_MODE ? 0.6F : 1.2F));
                        for (int i = 0; i < mi.damageTimeSupplier.get() -1; i++) {
                            addToBot(new DamageAction(p, this.damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                        addToBot(new ForceWaitAction(0.3f));
                        addToBot(new DamageAction(p, this.damage.get(mi.index), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                        int insectEggCount = ModHelper.getPowerCount(this, InsectEggPower.POWER_ID);
                        for (int i = 0; i < insectEggCount; i++) {
                            AbstractMonster monster = ModHelper.getRandomMonster(m -> m != this, true);
                            addToBot(new ApplyPowerAction(monster, this, new InsectEggPower(monster, 1)));
                        }
                        addToBot(new RemoveSpecificPowerAction(this, this, InsectEggPower.POWER_ID));
                        addToBot(new MakeTempCardInDrawPileAction(new Entangle(), 1, true, true));
                        addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
                    }
                });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ExtraToughnessPower(this, 3)));
        if (moreHPAs) {
            addToBot(new ApplyPowerAction(this, this, new DefensePower(this, defenseCount)));
        }
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(2);
        } else if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (canSpawn && hasPower(InsectEggPower.POWER_ID) && hasPower(MultiplyPower.POWER_ID)) {
            canSpawn = false;
            addToTop(new ReducePowerAction(this, this, InsectEggPower.POWER_ID, 1));
            spawnMonsters(1);
            rollMove();
            createIntent();
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (SubscriptionManager.checkSubscriber(this) && hasPower(InsectEggPower.POWER_ID) && hasPower(MultiplyPower.POWER_ID)) {
            canSpawn = true;
        }
    }
}
