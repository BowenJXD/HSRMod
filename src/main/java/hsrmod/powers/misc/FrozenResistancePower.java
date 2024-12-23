package hsrmod.powers.misc;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class FrozenResistancePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(FrozenResistancePower.class.getSimpleName());
    
    public FrozenResistancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        priority = 6;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
