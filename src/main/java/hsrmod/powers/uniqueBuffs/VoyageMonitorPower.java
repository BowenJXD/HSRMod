package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class VoyageMonitorPower extends PowerPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = HSRMod.makePath(VoyageMonitorPower.class.getSimpleName());

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
