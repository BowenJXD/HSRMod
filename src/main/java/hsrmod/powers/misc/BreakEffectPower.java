package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class BreakEffectPower extends BuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BreakEffectPower.class.getSimpleName());

    public BreakEffectPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount, this.amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this, true);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public float preDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        if (target.hasPower(BrokenPower.POWER_ID))
            amount += this.amount;
        return amount;
    }
}
