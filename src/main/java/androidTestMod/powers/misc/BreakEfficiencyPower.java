package androidTestMod.powers.misc;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import androidTestMod.subscribers.ISetToughnessReductionSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BreakEfficiencyPower extends BuffPower implements ISetToughnessReductionSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(BreakEfficiencyPower.class.getSimpleName());

    public static final float MULTIPLIER = 0.5f;

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
