package androidTestMod.powers.uniqueBuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.PowerPower;
import androidTestMod.powers.misc.DoTPower;
import androidTestMod.subscribers.PreDoTDamageSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ReignOfKeysPower extends PowerPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(ReignOfKeysPower.class.getSimpleName());

    boolean canTrigger = false;

    public ReignOfKeysPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
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
    public void atStartOfTurn() {
        super.atStartOfTurn();
        canTrigger = false;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        canTrigger = true;
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this)
                && (canTrigger || upgraded)
                && info.owner == owner) {
            flash();
            addToBot(new DrawCardAction(1));
        }
        return info.output;
    }
}
