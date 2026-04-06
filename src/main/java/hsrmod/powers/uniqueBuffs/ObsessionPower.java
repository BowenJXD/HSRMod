package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

public class ObsessionPower extends BuffPower {
    public static final String POWER_ID = hsrmod.modcore.HSRMod.makePath(ObsessionPower.class.getSimpleName());

    static final int STACK_LIMIT = 8;
    static final int REMOVE_AMOUNT = 2;
    
    public ObsessionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], STACK_LIMIT, amount, REMOVE_AMOUNT);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > STACK_LIMIT) {
            amount = STACK_LIMIT;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        if (amount > 0) {
            addToBot(new AddTemporaryHPAction(owner, owner, amount));
            remove(REMOVE_AMOUNT);
        }
    }
}
