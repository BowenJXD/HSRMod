package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.DoTPower;

public class BurnPower extends DoTPower {
    public static final String POWER_ID = HSRMod.makePath(BurnPower.class.getSimpleName());

    private int damage = 4;

    public BurnPower(AbstractCreature owner, AbstractCreature source, int Amount) {
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
        return ElementType.Fire;
    }
}
