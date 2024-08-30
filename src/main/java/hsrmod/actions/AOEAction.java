package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

public class AOEAction extends AbstractGameAction {
    private ArrayList<AbstractGameAction> actions = new ArrayList();

    public AOEAction(Function<AbstractMonster, AbstractGameAction> compute) {
        AbstractDungeon.getMonsters().monsters.stream().filter((m) -> {
            return !m.isDeadOrEscaped();
        }).forEach((q) -> {
            this.actions.add(compute.apply(q));
        });
    }
    
    public AOEAction(AbstractGameAction action){
        AbstractDungeon.getMonsters().monsters.stream().filter((m) -> {
            return !m.isDeadOrEscaped();
        }).forEach((q) -> {
            this.actions.add(action);
        });
    }

    public void update() {
        this.isDone = true;
        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator var1 = this.actions.iterator();

            while(var1.hasNext()) {
                AbstractGameAction action = (AbstractGameAction)var1.next();
                if (!action.isDone) {
                    action.update();
                    this.isDone = false;
                }
            }
        }

    }
}