package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.SporePower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class SporeDischargePower extends PowerPower implements PreEnergyChangeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SporeDischargePower.class.getSimpleName());
    
    public SporeDischargePower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && (changeAmount < 0 || (upgraded && changeAmount > 0))) {
            flash();
            AbstractCreature target = ModHelper.betterGetRandomMonster();
            if (target != null)
                addToBot(new ApplyPowerAction(target, owner, new SporePower(target, Math.abs(changeAmount))));
        }
        return changeAmount;
    }
}
