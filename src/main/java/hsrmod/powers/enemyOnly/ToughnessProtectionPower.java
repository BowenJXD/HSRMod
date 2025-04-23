package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.LockToughnessPower;

public class ToughnessProtectionPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ToughnessProtectionPower.class.getSimpleName());
    
    public ToughnessProtectionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        addToTop(new ApplyPowerAction(owner, owner, new LockToughnessPower(owner)));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        addToTop(new RemoveSpecificPowerAction(owner, owner, LockToughnessPower.POWER_ID));
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }
}
