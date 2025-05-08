package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.Hsrmod;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.DoTPower;

public class ShockPower extends DoTPower {
    public static final String POWER_ID = Hsrmod.makePath(ShockPower.class.getSimpleName());

    private int damage = 7;

    public ShockPower(AbstractCreature owner, AbstractCreature source, int Amount) {
        super(POWER_ID, owner, source, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], getDamage());
    }

    @Override
    public int getDamage() {
        return damage;
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.Lightning;
    }
}
