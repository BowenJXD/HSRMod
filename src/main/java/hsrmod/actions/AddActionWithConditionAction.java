package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.function.Predicate;

public class AddActionWithConditionAction extends AbstractGameAction {

    private final List<AbstractGameAction> action;
    private final Predicate<AbstractDungeon> condition;
    
    public AddActionWithConditionAction(List<AbstractGameAction> actions, Predicate<AbstractDungeon> condition) {
        this.action = actions;
        this.condition = condition;
    }

    public void update() {
/*        if (condition) {
            addToBot(action);
        }
        isDone = true;*/
    }

}
