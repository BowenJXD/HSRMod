package hsrmod.powers.uniqueDebuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.cards.uncommon.BlackSwan1;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import me.antileaf.signature.utils.SignatureHelper;

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
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
        BaseMod.subscribe(this);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = true);
        BaseMod.unsubscribe(this);
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        remove(1);
    }

    @Override
    public void receivePowersModified() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) && count >= 0) {
            count++;
            if (count >= 9) {
                count = -1;
                SignatureHelper.unlock(HSRMod.makePath(BlackSwan1.ID), true);
            }
        }
        return info.output;
    }
}
