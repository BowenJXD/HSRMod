package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class CloudflameLusterPower extends DebuffPower implements PreToughnessReduceSubscriber {
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
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        int newToughness = ModHelper.getPowerCount(target, ToughnessPower.POWER_ID) - (int) amount;
        if (SubscriptionManager.checkSubscriber(this)
                && ModHelper.getPowerCount(target, ToughnessPower.POWER_ID) > 0        
                && newToughness <= 0
                && target == this.owner) {
            this.flash();
            
            int amt = this.amount - newToughness;
            addToTop(new ApplyPowerAction(this.owner, this.owner, new ToughnessPower(this.owner, amt), amt));
            
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        return amount;
    }
}
