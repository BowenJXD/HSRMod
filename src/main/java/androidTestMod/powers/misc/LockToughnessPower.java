package androidTestMod.powers.misc;

import androidTestMod.AndroidTestMod;
import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LockToughnessPower extends AbstractPower {
    public static final String POWER_ID = AndroidTestMod.makePath(LockToughnessPower.class.getSimpleName());

    public LockToughnessPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.amount = 0;
        this.owner = owner;
        this.type = CustomEnums.STATUS;
    }
}
