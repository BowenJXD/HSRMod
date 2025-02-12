package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ReignOfKeysPower extends PowerPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ReignOfKeysPower.class.getSimpleName());

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
