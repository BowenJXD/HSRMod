package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

public class StrengthenPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(StrengthenPower.class.getSimpleName());
    
    public StrengthenPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }
}
