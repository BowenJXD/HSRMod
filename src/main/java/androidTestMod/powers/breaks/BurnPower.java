package androidTestMod.powers.breaks;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.ElementType;
import androidTestMod.powers.misc.DoTPower;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BurnPower extends DoTPower {
    public static final String POWER_ID = AndroidTestMod.makePath(BurnPower.class.getSimpleName());

    private int damage = 6;

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
