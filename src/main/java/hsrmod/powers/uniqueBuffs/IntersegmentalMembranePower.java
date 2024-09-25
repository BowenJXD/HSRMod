package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class IntersegmentalMembranePower extends PowerPower implements PreEnergyChangeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(IntersegmentalMembranePower.class.getSimpleName());

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
        SubscribeManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscribeManager.getInstance().unsubscribe(this);
    }

    @Override
    public int receivePreEnergyChange(int changeAmount) {
        if (SubscribeManager.checkSubscriber(this) && changeAmount < 0) {
            flash();
            addToBot(new GainBlockAction(owner, owner, -changeAmount * block));
        }
        return changeAmount;
    }
}
