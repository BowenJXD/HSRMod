package androidTestMod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;
import androidTestMod.powers.misc.QuakePower;
import androidTestMod.subscribers.PostBreakBlockSubscriber;
import androidTestMod.subscribers.SubscriptionManager;

public class ResonanceTransferPower extends PowerPower implements PostBreakBlockSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(ResonanceTransferPower.class.getSimpleName());

    public ResonanceTransferPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0]);
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
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this) && c != owner) {
            trigger(c);
        }
    }

    public void trigger(AbstractCreature c) {
        this.flash();
        addToTop(new ApplyPowerAction(owner, owner, new QuakePower(owner, 1), 1));
    }
}
