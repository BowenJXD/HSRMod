package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class ReignOfKeysPower extends PowerPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ReignOfKeysPower.class.getSimpleName());
    
    boolean canTrigger = false;
    
    public ReignOfKeysPower() {
        super(POWER_ID);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscribeManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscribeManager.getInstance().unsubscribe(this);
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
    public float preDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        if (SubscribeManager.checkSubscriber(this)
        && (canTrigger || upgraded)) {
            flash();
            addToBot(new DrawCardAction(1));
        }
        return amount;
    }
}