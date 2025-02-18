package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.misc.IMultiToughness;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class ExtraToughnessPower extends BuffPower implements PreElementalDamageSubscriber, IMultiToughness {
    public static final String POWER_ID = HSRMod.makePath(ExtraToughnessPower.class.getSimpleName());

    public ExtraToughnessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        priority = 9;
        updateDescription();
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
    public int getToughnessBarCount() {
        return amount;
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.target == owner
                && owner.hasPower(ToughnessPower.POWER_ID)
                && action.info.tr >= ModHelper.getPowerCount(owner, ToughnessPower.POWER_ID)) {
            if (amount > 1) {
                ToughnessPower power = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
                action.info.tr -= power.amount;
                if (action.info.tr < 0) {
                    action.info.tr = 0;
                }
                power.alterPower(ToughnessPower.getStackLimit(owner));
            }
            remove(1);
        }
        return dmg;
    }
}
