package hsrmod.minion;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class PlayerMinionTakeTurnAction extends AbstractGameAction {
    public PlayerMinionTakeTurnAction() {
    }

    public void update() {
        if (!MinionGroup.areMinionsBasicallyDead()) {
            AbstractPlayerMinion minion = MinionGroup.getCurrentMinion();
            if (minion != null) {
                minion.takeTurn();
            }
        }

        this.isDone = true;
    }
}
