package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.LockToughnessAction;
import hsrmod.actions.UnlockToughnessAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class ShadowOfFeixiao extends BaseMonster implements PreBreakSubscriber {
    public static final String ID = ShadowOfFeixiao.class.getSimpleName();

    int resonateCount = 4;
    int toughnessHealCount = 4;

    public ShadowOfFeixiao() {
        super(ID, 256, 400, -20, -40);
        bgm = "Inner Beast Vanquished";

        toughnessHealCount = moreHPAs ? 6 : 4;

        addMove(Intent.ATTACK_DEBUFF, 3 * 2, mi -> {
            if (AbstractDungeon.miscRng.randomBoolean()) {
                shout(1, 3f);   
            }
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
            addToBot(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
        });
        addMove(Intent.BUFF, mi -> {
            addToBot(new ApplyPowerAction(this, this, new ResonatePower(this, resonateCount), 0));
            if (ModHelper.getPowerCount(this, ToughnessPower.POWER_ID) > 0)
                addToBot(new ApplyPowerAction(this, this, new ToughnessPower(this, toughnessHealCount)));
            if (specialAs)
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
            AbstractMonster monster = ModHelper.getRandomMonster(m -> m != this && !m.hasPower(ResonatePower.POWER_ID), true);
            if (monster != null) {
                if (monster instanceof NebulaDevourer) shout(2, 3f);
                else if (monster instanceof PlaneshredClaws) shout(3, 3f);
                else if (monster instanceof WorldpurgeTail) shout(4, 3f);

                addToBot(new ApplyPowerAction(monster, this, new ResonatePower(monster, resonateCount), 0));
                addToBot(new UnlockToughnessAction(monster, monster));
                if (ModHelper.getPowerCount(monster, ToughnessPower.POWER_ID) > 0)
                    addToBot(new ApplyPowerAction(monster, this, new ToughnessPower(monster, toughnessHealCount)));
                if (specialAs)
                    addToBot(new ApplyPowerAction(monster, this, new StrengthPower(monster, 1)));
            }
        });
        addMove(Intent.BUFF, mi -> {
            shout(5, 3f);
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> ModHelper.check(m) && !m.hasPower(ResonatePower.POWER_ID))
                    .forEach(m -> {
                                addToBot(new ApplyPowerAction(m, this, new ResonatePower(m, resonateCount), 0));
                                addToBot(new UnlockToughnessAction(m, m));
                                if (ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) > 0)
                                    addToBot(new ApplyPowerAction(m, this, new ToughnessPower(m, toughnessHealCount)));
                            }
                    );
            addToBot(new LockToughnessAction(this, this));
            addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            SubscriptionManager.subscribe(this);
        });
        addMoveA(Intent.ATTACK, 9, () -> {
            return AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> ModHelper.check(m) && ModHelper.getPowerCount(m, ToughnessPower.POWER_ID) > 0 ? 1 : 0).sum();
        }, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                shout(6, 3f);
                attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
                AbstractDungeon.getMonsters().monsters.forEach(m -> {
                    addToBot(new RemoveSpecificPowerAction(m, this, ResonatePower.POWER_ID));
                });
            }
            addToBot(new UnlockToughnessAction(this, this));
            SubscriptionManager.unsubscribe(this);
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 0, 2f));
        AbstractDungeon.getMonsters().monsters.forEach(m -> {
            if (m != this) {
                addToBot(new ApplyPowerAction(m, this, new SummonedPower(m)));
            }
        });
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(3);
            return;
        }
        int nonResonateCount = AbstractDungeon.getMonsters().monsters.stream().mapToInt(m -> ModHelper.check(m) && !m.hasPower(ResonatePower.POWER_ID) ? 1 : 0).sum();
        if (nonResonateCount <= 1 || (currentHealth < i * 2)) {
            setMove(2);
        } else if (hasPower(ResonatePower.POWER_ID) || lastMove((byte) 0) || specialAs) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 7, 2f));
        ModHelper.killAllMinions();
        onBossVictoryLogic();
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && hasPower(ChargingPower.POWER_ID)) {
            ModHelper.addToBotAbstract(() -> {
                setMove(3);
                createIntent();
            });
        }
    }
}
