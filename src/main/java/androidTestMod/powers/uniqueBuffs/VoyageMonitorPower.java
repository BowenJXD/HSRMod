package androidTestMod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.ElementType;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;
import androidTestMod.subscribers.PreToughnessReduceSubscriber;
import androidTestMod.subscribers.SubscriptionManager;

public class VoyageMonitorPower extends PowerPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(VoyageMonitorPower.class.getSimpleName());

    public int triggerAmount = 2;
    
    public VoyageMonitorPower(int triggerAmount) {
        super(POWER_ID);
        this.triggerAmount = triggerAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], triggerAmount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        int blk = amount / triggerAmount;
        if (blk > 0) {
            this.flash();
            addToBot(new GainBlockAction(owner, owner, blk));
            amount -= blk * triggerAmount;
        }
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
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this) && amount > 0) {
            stackPower((int)amount);
        }
        return amount;
    }
}
