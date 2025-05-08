package hsrmod.powers.misc;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.Hsrmod;
import hsrmod.modcore.CustomEnums;

public class LockToughnessPower extends AbstractPower {
    public static final String POWER_ID = Hsrmod.makePath(LockToughnessPower.class.getSimpleName());

    public LockToughnessPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.amount = 0;
        this.owner = owner;
        this.type = CustomEnums.STATUS;
    }
}
