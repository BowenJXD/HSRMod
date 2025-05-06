package androidTestMod.powers.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnReceivePowerPower {
    boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source);

    default int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        return stackAmount;
    }
}
