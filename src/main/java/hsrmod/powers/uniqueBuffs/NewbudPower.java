package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

public class NewbudPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(NewbudPower.class.getSimpleName());

    public NewbudPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }
}
