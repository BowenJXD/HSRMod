package androidTestMod.powers.uniqueDebuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.DebuffPower;
import androidTestMod.powers.misc.BrokenPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ThanatoplumRebloomPower extends DebuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(ThanatoplumRebloomPower.class.getSimpleName());

    public ThanatoplumRebloomPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        priority = 7;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
            remove(1);
        }
    }
}
