package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.DoTPower;

public class BleedingPower extends DoTPower {
    public static final String POWER_ID = HSRMod.makePath(BleedingPower.class.getSimpleName());

    private int damagePercentage = 5;
    
    private int damageLimit = 10;
    
    boolean removeLimit = false;
    
    public BleedingPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(POWER_ID, owner, source, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (removeLimit) {
            this.description = String.format(DESCRIPTIONS[1], getDamage(), damagePercentage);
        } else {
            this.description = String.format(DESCRIPTIONS[0], getDamage(), damagePercentage, damageLimit);
        }
    }

    @Override
    public int getDamage() {
        if (removeLimit) {
            return Math.round(owner.maxHealth * damagePercentage / 100f);
        }
        return Math.min(Math.round(owner.maxHealth * damagePercentage / 100f), damageLimit);
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.Physical;
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        removeLimit = true;
    }
}
