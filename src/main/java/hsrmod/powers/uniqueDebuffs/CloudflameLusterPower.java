package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.cardsV2.Fugue1;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class CloudflameLusterPower extends DebuffPower implements PreBreakSubscriber {
    public static final String ID = HSRMod.makePath(CloudflameLusterPower.class.getSimpleName());
    
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
            if (amt > 0)
                addToTop(new ApplyPowerAction(this.owner, this.owner, new ToughnessPower(this.owner, amt), amt));
            else if (amt < 0)
                addToTop(new ReducePowerAction(this.owner, this.owner, new ToughnessPower(this.owner, -amt), -amt));

            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            
            if (amount >= ToughnessPower.getStackLimit(owner)) {
                SignatureHelper.unlock(HSRMod.makePath(Fugue1.ID), true);
            }
        }
    }
}
