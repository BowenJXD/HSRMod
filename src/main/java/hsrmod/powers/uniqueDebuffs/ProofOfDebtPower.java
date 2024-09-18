package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class ProofOfDebtPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(ProofOfDebtPower.class.getSimpleName());
    
    public ProofOfDebtPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}
