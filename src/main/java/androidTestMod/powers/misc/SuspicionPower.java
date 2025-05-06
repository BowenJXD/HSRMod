package androidTestMod.powers.misc;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.DebuffPower;
import androidTestMod.subscribers.PreDoTDamageSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SuspicionPower extends DebuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(SuspicionPower.class.getSimpleName());

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
        SubscriptionManager.subscribe(this, true);
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
        SubscriptionManager.unsubscribe(this);
    }
    
    public float incrementDamage(float damage) {
        damage *= (1 + damageIncrementPercentage * this.amount);
        return damage;
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) 
                && target == owner) {
            return incrementDamage(info.output);
        }
        return info.output;
    }
}
