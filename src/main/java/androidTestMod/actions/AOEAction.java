package androidTestMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

public class AOEAction extends AbstractGameAction {
    private ArrayList<AbstractGameAction> actions = new ArrayList();

    public AOEAction(Function<AbstractMonster, AbstractGameAction> compute) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                this.actions.add(compute.apply(m));
            }
        }
    }
    
    public AOEAction(AbstractGameAction action){
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                this.actions.add(action);
            }
        }
    }

    public void update() {
        this.isDone = true;
        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator var1 = this.actions.iterator();

            while(var1.hasNext()) {
                AbstractGameAction action = (AbstractGameAction)var1.next();
                if (action == null) continue;
                if (!action.isDone) {
                    action.update();
                    this.isDone = false;
                }
            }
        }

    }
}