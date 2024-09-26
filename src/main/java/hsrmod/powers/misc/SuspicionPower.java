package hsrmod.powers.misc;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class SuspicionPower extends DebuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SuspicionPower.class.getSimpleName());

    private final float damageIncrementPercentage = 1f / 100f;
    
    int stackLimit = 99;

    public SuspicionPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageIncrementPercentage * amount * 100), stackLimit);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount > stackLimit) {
            this.amount = stackLimit;
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }
    
    public float incrementDamage(float damage) {
        damage *= (1 + damageIncrementPercentage * this.amount);
        return damage;
    }

    @Override
    public float preDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) 
                && target == owner) {
            return incrementDamage(amount);
        }
        return amount;
    }
}
