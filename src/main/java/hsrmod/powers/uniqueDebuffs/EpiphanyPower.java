package hsrmod.powers.uniqueDebuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class EpiphanyPower extends DebuffPower implements OnPowersModifiedSubscriber, PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(EpiphanyPower.class.getSimpleName());

    int count = 0;
    
    public EpiphanyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        for (AbstractPower power : owner.powers) {
            if (power instanceof DoTPower) {
                DoTPower p = (DoTPower) power;
                p.removeOnTrigger = false;
            }
        }
        BaseMod.subscribe(this);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        for (AbstractPower power : owner.powers) {
            if (power instanceof DoTPower) {
                DoTPower p = (DoTPower) power;
                p.removeOnTrigger = true;
            }
        }
        BaseMod.unsubscribe(this);
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        remove(1);
    }

    @Override
    public void receivePowersModified() {
        for (AbstractPower power : owner.powers) {
            if (power instanceof DoTPower) {
                DoTPower p = (DoTPower) power;
                p.removeOnTrigger = false;
            }
        }
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) && count >= 0) {
            count++;
            if (count >= 9) {
                count = -1;
            }
        }
        return info.output;
    }
}
