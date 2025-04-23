package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;

public class LockToughnessPower extends AbstractPower implements InvisiblePower {
    public static final String POWER_ID = HSRMod.makePath(LockToughnessPower.class.getSimpleName());

    public LockToughnessPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.amount = 0;
        this.owner = owner;
        this.type = NeutralPowertypePatch.NEUTRAL;
    }
}
