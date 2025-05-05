package androidTestMod.powers.misc;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;

public class BrainInAVatPower extends BuffPower implements OnReceivePowerPower {
    public static final String POWER_ID = AndroidTestMod.makePath(BrainInAVatPower.class.getSimpleName());

    public BrainInAVatPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof EnergyPower
                && stackAmount < 0) {
            flash();
            EnergyPower energyPower = (EnergyPower) target.getPower(EnergyPower.POWER_ID);
            if (energyPower == null || !energyPower.isLocked())
                addToTop(new ApplyPowerAction(owner, owner, new EnergyPower(owner, -stackAmount)));
            remove(1);
        }
        return stackAmount;
    }
}
