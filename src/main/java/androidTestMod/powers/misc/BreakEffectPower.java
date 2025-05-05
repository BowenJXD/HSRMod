package androidTestMod.powers.misc;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import androidTestMod.subscribers.PreDoTDamageSubscriber;
import androidTestMod.subscribers.SubscriptionManager;

public class BreakEffectPower extends BuffPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(BreakEffectPower.class.getSimpleName());

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
