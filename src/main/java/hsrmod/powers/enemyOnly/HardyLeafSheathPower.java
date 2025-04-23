package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.LockToughnessPower;

public class HardyLeafSheathPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(HardyLeafSheathPower.class.getSimpleName());
    
    public HardyLeafSheathPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        addToBot(new ApplyPowerAction(owner, owner, new LockToughnessPower(owner)));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        addToBot(new RemoveSpecificPowerAction(owner, owner, LockToughnessPower.POWER_ID));
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        flash();
        return damageAmount / 2;
    }
}
