package androidTestMod.powers.misc;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FrozenResistancePower extends BuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(FrozenResistancePower.class.getSimpleName());
    
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
