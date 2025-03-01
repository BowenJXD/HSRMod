package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cardsV2.Curse.Entangle;
import hsrmod.misc.IMultiToughness;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class ExtraToughnessPower extends BuffPower implements PreToughnessReduceSubscriber, IMultiToughness {
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
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this) 
                && target == owner
                && owner.hasPower(ToughnessPower.POWER_ID)
                && amount >= ModHelper.getPowerCount(owner, ToughnessPower.POWER_ID)) {
            if (this.amount > 1) {
                ToughnessPower power = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
                amount -= power.amount;
                if (amount < 0) {
                    amount = 0;
                }
                power.alterPower(ToughnessPower.getStackLimit(owner));
            }
            remove(1);
            addToBot(new MakeTempCardInHandAction(new Entangle()));
        }
        return amount;
    }
}
