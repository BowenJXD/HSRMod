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

    boolean triggeredThisTurn = false;
    
    public SymbiotePower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }
    
    @Override
    public void onInitialApplication() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        triggeredThisTurn = false;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower) {
            if (!triggeredThisTurn) {
                this.flash();
                addToTop(new GainEnergyAction(2));
            }
            else if (upgraded) {
                this.flash();
                addToTop(new GainEnergyAction(1));
            }
            triggeredThisTurn = true;
        }
    }

    @Override
    public float preBreakDamage(float amount, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            addToTop(new DrawCardAction(1));
        }
        return amount;
    }
}
