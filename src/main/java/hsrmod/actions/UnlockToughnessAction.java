package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.powers.misc.ToughnessPower;

public class UnlockToughnessAction extends AbstractGameAction {
    public UnlockToughnessAction(AbstractCreature target, Object source) {
        this.target = target;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        isDone = true;
        ToughnessPower toughnessPower = (ToughnessPower) target.getPower(ToughnessPower.POWER_ID);
        if (toughnessPower != null) {
            toughnessPower.unlock(source);
        }
    }
}
