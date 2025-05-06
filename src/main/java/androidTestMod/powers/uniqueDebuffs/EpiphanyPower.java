package androidTestMod.powers.uniqueDebuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.DebuffPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;
import androidTestMod.powers.misc.DoTPower;
import androidTestMod.subscribers.PreDoTDamageSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EpiphanyPower extends DebuffPower implements OnReceivePowerPower, PreDoTDamageSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(EpiphanyPower.class.getSimpleName());

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
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        remove(1);
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

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == owner && power instanceof DoTPower) {
            ((DoTPower)power).removeOnTrigger = false;
        }
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        return stackAmount;
    }
}
