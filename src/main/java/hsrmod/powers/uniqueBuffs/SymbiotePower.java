package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class SymbiotePower extends PowerPower implements PreBreakDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SymbiotePower.class.getSimpleName());

    public SymbiotePower() {
        super(POWER_ID);
        this.updateDescription();
    }
    
    @Override
    public void onInitialApplication() {
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower) {
            this.flash();
            addToBot(new GainEnergyAction(2));
        }
    }

    @Override
    public float preBreakDamage(float amount, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            addToBot(new DrawCardAction(1));
        }
        return amount;
    }
}
