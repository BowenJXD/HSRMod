package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.Hsrmod;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.DoTPower;

public class WindShearPower extends DoTPower {
    public static final String POWER_ID = Hsrmod.makePath(WindShearPower.class.getSimpleName());

    private int damage = 2;
    
    private final int damageCountLimit = 5;

    public WindShearPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(POWER_ID, owner, source, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], getDamage(), damage, damageCountLimit);
    }

    @Override
    public int getDamage() {
        return damage * Math.min(amount, damageCountLimit);
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.Wind;
    }
}
