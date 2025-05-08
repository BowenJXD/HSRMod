package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.interfaces.OnReceivePowerPower;
import hsrmod.powers.misc.EnergyPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class KolchisPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = Hsrmod.makePath(KolchisPower.class.getSimpleName());

    int chargeThreshold = 100;
    
    public KolchisPower(int chargeThreshold) {
        super(POWER_ID);
        this.chargeThreshold = chargeThreshold;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], chargeThreshold);
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

            amount += -stackAmount;
            if (amount >= chargeThreshold) {
                amount = 0;
                addToBot(new GainEnergyAction(1));
            }
        }
        return stackAmount;
    }
}
