package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.DoTPower;

public class BleedingPower extends DoTPower {
    public static final String POWER_ID = HSRMod.makePath(BleedingPower.class.getSimpleName());

    private int damagePercentage = 5;
    
    private int damageLimit = 10;
    
    public BleedingPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(POWER_ID, owner, source, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], getDamage(), damagePercentage, damageLimit);
    }

    @Override
    public int getDamage() {
        return Math.min(Math.round(owner.maxHealth * damagePercentage / 100f), damageLimit);
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.Physical;
    }
}
