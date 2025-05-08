package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

public class IntersegmentalMembranePower extends PowerPower implements PreEnergyChangeSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(IntersegmentalMembranePower.class.getSimpleName());

    int block;
    
    public IntersegmentalMembranePower(int block) {
        super(POWER_ID);
        this.block = block;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.block);
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
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this) && changeAmount < 0) {
            flash();
            int num = -changeAmount * block;
            addToBot(new GainBlockAction(owner, owner, num));
        }
        return changeAmount;
    }
}
