package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;

public class TrotterBlessingPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(TrotterBlessingPower.class.getSimpleName());
    
    public TrotterBlessingPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        
        updateDescription();
    }
}
