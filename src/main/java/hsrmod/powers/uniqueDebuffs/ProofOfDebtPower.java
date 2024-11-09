package hsrmod.powers.uniqueDebuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ProofOfDebtPower extends DebuffPower implements PreElementalDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ProofOfDebtPower.class.getSimpleName());

    public ProofOfDebtPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
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
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)
                && action.target == this.owner
                && action.info.card != null
                && action.info.card.hasTag(CustomEnums.FOLLOW_UP)) {
            return dmg + this.amount;
        }
        return dmg;
    }
}
