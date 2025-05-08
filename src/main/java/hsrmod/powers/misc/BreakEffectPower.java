package hsrmod.powers.misc;

import hsrmod.Hsrmod;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BreakEffectPower extends BuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(BreakEffectPower.class.getSimpleName());

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
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) 
                && target.hasPower(BrokenPower.POWER_ID)
                && info.owner == owner)
            info.output += this.amount;
        return info.output;
    }
}
