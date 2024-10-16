package hsrmod.powers.uniqueDebuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.DoTPower;

public class EpiphanyPower extends DebuffPower implements OnPowersModifiedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(EpiphanyPower.class.getSimpleName());

    public EpiphanyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = true);
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        remove(1);
    }

    @Override
    public void receivePowersModified() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
    }
}
