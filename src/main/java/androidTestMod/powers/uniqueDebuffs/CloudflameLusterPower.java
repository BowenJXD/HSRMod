package androidTestMod.powers.uniqueDebuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.DebuffPower;
import androidTestMod.powers.misc.ToughnessPower;
import androidTestMod.subscribers.PreBreakSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class CloudflameLusterPower extends DebuffPower implements PreBreakSubscriber {
    public static final String ID = AndroidTestMod.makePath(CloudflameLusterPower.class.getSimpleName());
    
    public CloudflameLusterPower(AbstractCreature owner, int amount) {
        super(ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        description = String.format(DESCRIPTIONS[0], amount);
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
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == this.owner) {
            this.flash();

            int newToughness = ModHelper.getPowerCount(target, ToughnessPower.POWER_ID) - info.tr;
            int amt = this.amount - newToughness;
            addToTop(new ApplyPowerAction(this.owner, this.owner, new ToughnessPower(this.owner, amt), amt));
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
