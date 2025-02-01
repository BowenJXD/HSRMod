package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class SpurOfThunderwoePower extends DebuffPower implements NonStackablePower {
    public static final String POWER_ID = HSRMod.makePath(SpurOfThunderwoePower.class.getSimpleName());
    
    public SpurOfThunderwoePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }
}
