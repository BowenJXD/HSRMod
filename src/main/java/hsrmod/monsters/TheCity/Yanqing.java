package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.FormationCorePower;
import hsrmod.powers.enemyOnly.SwordFormationPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Yanqing extends BaseMonster implements PostMonsterDeathSubscriber {
    public static final String ID = Yanqing.class.getSimpleName();

    List<FormationCorePower.Tag> tags = new ArrayList<>(Arrays.asList(
            FormationCorePower.Tag.EXHAUST,
            FormationCorePower.Tag.ETHEREAL,
            FormationCorePower.Tag.INNATE,
            FormationCorePower.Tag.RETAIN
    ));
    int frozenCount = 1;

    public Yanqing() {
        super(ID, 200, 400, -100, 0);
        bgm = "Gleaming Clash";
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;

        // Collections.shuffle(tags);
        addSlot(-450, 0);
        addSlot(-300, 0);
        addSlot(100, 0);
        addSlot(250, 0);
        monFunc = slot -> new FlyingSword(slot.x, slot.y, tags.get(slots.indexOf(slot)));
        
        frozenCount = specialAs ? 2 : 1;

        addMove(Intent.UNKNOWN, mi -> {
            shout(0);
            spawnMonsters();
            addToBot(new ApplyPowerAction(this, this, new SwordFormationPower(this)));
            addToBot(new ApplyPowerAction(this, this, new LockToughnessPower(this)));
            AbstractDungeon.player.drawPile.group.forEach(c -> CommonKeywordIconsField.useIcons.set(c, true));
            if (!AbstractDungeon.player.drawPile.isEmpty()) {
                AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.aiRng).exhaust = true;
                AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.aiRng).isEthereal = true;
                AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.aiRng).isInnate = true;
                AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.aiRng).selfRetain = true;
            }
        });
        addMove(Intent.ATTACK_DEBUFF, 3, 4, mi -> {
            shoutIf(1);
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
        });
        addMove(Intent.DEBUFF, mi -> {
            addToBot(new MakeTempCardInDrawPileAction(new Frozen(), frozenCount, true, true));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[6])));
        });
        addMoveA(Intent.ATTACK, 15, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                shoutIf(2);
                addToBot(new VFXAction(new WeightyImpactEffect(p.hb.cX, p.hb.cY, Color.CYAN), 1f));
                attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
                AbstractDungeon.player.hand.group.stream()
                        .filter(c -> c instanceof Frozen)
                        .forEach(c -> addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand)));
            }
        });
        addMove(Intent.UNKNOWN, mi -> {
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            SubscriptionManager.subscribe(this);
        });
        addMoveA(Intent.ATTACK, 6,
                () -> 1 + AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).mapToInt(m -> m.hasPower(FormationCorePower.POWER_ID) ? 1 : 0).sum(),
                mi -> {
                    if (hasPower(ChargingPower.POWER_ID)) {
                        shout(3, 4);
                        addToBot(new VFXAction(new WeightyImpactEffect(p.hb.cX, p.hb.cY, Color.CYAN), 1f));
                        attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    }
                    SubscriptionManager.unsubscribe(this);
                }
        );
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            if (hasPower(SwordFormationPower.POWER_ID)) {
                setMove(5);
            } else {
                setMove(3);
            }
            turnCount = 1;
        } else if (turnCount == 0) {
            setMove(0);
            turnCount++;
        } else if (turnCount == 1) {
            setMove(1);
            turnCount++;
        } else if (turnCount == 2) {
            if (hasPower(SwordFormationPower.POWER_ID)) {
                setMove(4);
            } else {
                setMove(2);
            }
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 5, 2f));
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this) && hasPower(ChargingPower.POWER_ID)) {
            rollMove();
            createIntent();
        }
    }
}
