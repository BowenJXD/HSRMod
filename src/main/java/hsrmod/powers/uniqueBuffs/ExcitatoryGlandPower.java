package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ExcitatoryGlandPower extends PowerPower implements PreEnergyChangeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ExcitatoryGlandPower.class.getSimpleName());
    
    public int triggerAmount = 5;
    
    public ExcitatoryGlandPower(int triggerAmount) {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], triggerAmount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= triggerAmount) {
            flash();
            amount -= triggerAmount;
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && changeAmount < 0) {
            stackPower(-changeAmount);
        }
        return changeAmount;
    }
}
