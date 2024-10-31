package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.ISetToughnessReductionSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class BreakEfficiencyPower extends BuffPower implements ISetToughnessReductionSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BreakEfficiencyPower.class.getSimpleName());

    public static final float MULTIPLIER = 1f;

    public BreakEfficiencyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], (int) (MULTIPLIER * 100));
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this, true);
    }

    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public int setToughnessReduction(AbstractCreature target, int amt) {
        if (SubscriptionManager.checkSubscriber(this))
            return amt + (int) ((float)amt * MULTIPLIER);
        return amt;
    }
}
