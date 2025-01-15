package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.utils.ModHelper;

public class CleanAction extends AbstractGameAction {
    boolean removeAll;

    public CleanAction(AbstractCreature target, int amount, boolean removeAll) {
        this.target = target;
        this.amount = amount;
        this.removeAll = removeAll;
    }

    @Override
    public void update() {
        isDone = true;
        for (AbstractPower power : target.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                int removeAmount = removeAll ? 1 : Math.min(amount, power.amount);

                if (removeAll || power.amount == removeAmount) {
                    addToBot(new RemoveSpecificPowerAction(target, target, power));
                } else {
                    addToBot(new ReducePowerAction(target, target, power, removeAmount));
                }

                amount -= removeAmount;
                if (amount <= 0) break;
            }
        }
    }
}
