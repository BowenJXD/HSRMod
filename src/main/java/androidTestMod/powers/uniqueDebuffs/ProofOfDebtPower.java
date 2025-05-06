package androidTestMod.powers.uniqueDebuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.DebuffPower;
import androidTestMod.subscribers.PreElementalDamageSubscriber;
import androidTestMod.subscribers.PreFollowUpSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ProofOfDebtPower extends DebuffPower implements PreFollowUpSubscriber, PreElementalDamageSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(ProofOfDebtPower.class.getSimpleName());

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
        SubscriptionManager.subscribe(this, true);
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

    @Override
    public AbstractCreature preFollowUpAction(AbstractCard card, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && ModHelper.check(owner)) {
            card.damage += this.amount;
            return owner;
        }
        return target;
    }
}
