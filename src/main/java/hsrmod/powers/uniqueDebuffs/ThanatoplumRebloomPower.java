package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.misc.ToughnessPower;

public class ThanatoplumRebloomPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(ThanatoplumRebloomPower.class.getSimpleName());

    public ThanatoplumRebloomPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        priority = 7;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
            remove(1);
        }
        addToBot(new ApplyPowerAction(owner, owner, new LockToughnessPower(owner)));
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        addToBot(new RemoveSpecificPowerAction(owner, owner, LockToughnessPower.POWER_ID));
    }
}
