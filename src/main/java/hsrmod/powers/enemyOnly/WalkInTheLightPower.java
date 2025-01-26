package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.misc.IMultiToughness;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class WalkInTheLightPower extends StatePower implements PreBreakSubscriber, IMultiToughness {
    public static final String POWER_ID = HSRMod.makePath(WalkInTheLightPower.class.getSimpleName());

    int tempHPAmount = 10;
    int blockAmount = 7;
    int damageMultiplier = 10;
    int damageMultiplier2 = 200;
    
    public WalkInTheLightPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }
    
    public void updateDescription() {
        if (amount > 0)
            if (ModHelper.specialAscension(AbstractMonster.EnemyType.BOSS))
                this.description = String.format(DESCRIPTIONS[1], amount * damageMultiplier, tempHPAmount, blockAmount, damageMultiplier2);
            else
                this.description = String.format(DESCRIPTIONS[0], amount * damageMultiplier, tempHPAmount, blockAmount, damageMultiplier2);
        else
            this.description = String.format(DESCRIPTIONS[2], damageMultiplier2);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void stackPower(int stackAmount) {
        alterPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }
    
    void alterPower(int alterAmount) {
        this.fontScale = 8.0F;
        this.amount += alterAmount;

        if (amount < 0) {
            amount = 0;
            return;
        }
        if (alterAmount < 0) {
            addToBot(new ApplyPowerAction(owner, owner, new ToughnessPower(owner, ToughnessPower.getStackLimit(owner) * 2), ToughnessPower.getStackLimit(owner) * 2));
            addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, tempHPAmount));
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, blockAmount));
            if (ModHelper.specialAscension(AbstractMonster.EnemyType.BOSS)) {
                addImprison();
            }
        }
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            if (amount > 0) return damage * (1 - amount * damageMultiplier / 100f);
            else return damage * (1 + damageMultiplier2 / 100f);
        return damage;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == owner
                && amount > 0) {
            reducePower(1);
            if (!ModHelper.specialAscension(AbstractMonster.EnemyType.BOSS))
                addImprison();
        }
    }
    
    void addImprison() {
        if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE)
            addToBot(new MakeTempCardInDiscardAction(new Imprison(), 1));
        else
            addToBot(new MakeTempCardInHandAction(new Imprison(), 1));
    }

    @Override
    public int getToughnessBarCount() {
        return amount;
    }
}
