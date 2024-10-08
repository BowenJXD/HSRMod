package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class BreakEfficiencyPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(BreakEfficiencyPower.class.getSimpleName());

    public static final float MULTIPLIER = 1f / 2f;
    
    public int minAmount = 0;

    public BreakEfficiencyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    public void updateDescription() {
        if (minAmount == 0)
            this.description = String.format(DESCRIPTIONS[0], (int) (this.amount * MULTIPLIER * 100));
        else 
            this.description = String.format(DESCRIPTIONS[1], (int) (this.amount * MULTIPLIER * 100), minAmount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount = Math.max(this.amount, minAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        this.amount = Math.max(this.amount, minAmount);
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atStartOfTurn();
        reducePower(1);
    }
}
