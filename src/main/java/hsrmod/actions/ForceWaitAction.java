package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ForceWaitAction extends AbstractGameAction {
    public ForceWaitAction(float setDuration) {
        this.setValues((AbstractCreature) null, (AbstractCreature) null, 0);
        this.duration = setDuration;

        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        this.tickDuration();
    }
}
