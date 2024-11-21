package hsrmod.powers.misc;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class EnergyPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(EnergyPower.class.getSimpleName());
    
    boolean locked = false;
    public static final int AMOUNT_LIMIT = 240;

    public EnergyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], AMOUNT_LIMIT);
    }

    @Override
    public void stackPower(int stackAmount) {
        alterPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }
    
    void alterPower(int alterAmount) {
        if (locked) {
            return;
        }
        this.fontScale = 8.0F;
        this.amount += alterAmount;
        
        if (amount > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        } else if (amount < 0) {
            amount = 0;
        }
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public boolean getLocked() {
        return locked;
    }
}
