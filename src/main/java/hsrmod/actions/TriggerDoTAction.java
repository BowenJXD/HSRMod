package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.uniqueBuffs.ReignOfKeysPower;

import java.util.List;
import java.util.stream.Collectors;

public class TriggerDoTAction extends AbstractGameAction {
    String powerID;
    boolean triggerAll;
    
    public TriggerDoTAction(AbstractCreature target, int amount, boolean triggerAll, String powerID) {
        this.target = target;
        this.source = AbstractDungeon.player;
        this.powerID = powerID;
        this.amount = amount;
        this.triggerAll = triggerAll;
    }
    
    public TriggerDoTAction(AbstractCreature target, int amount, String powerID) {
        this(target, amount, false, powerID);
    }
    
    public TriggerDoTAction(AbstractCreature target, int amount, boolean triggerAll) {
        this(target, amount, triggerAll, null);
    }
    
    public TriggerDoTAction(AbstractCreature target, int amount) {
        this(target, amount, false, null);
    }
    
    public TriggerDoTAction(AbstractCreature target) {
        this(target, 1, false, null);
    }

    public void update() {
        this.isDone = true;
        
        List<DoTPower> dots = target.powers.stream()
                .filter(p -> p instanceof DoTPower 
                        && (p.ID.equals(powerID) || powerID == null))
                .map(p -> (DoTPower) p)
                .collect(Collectors.toList());
        
        if (dots.isEmpty()) return;
        
        if (--amount > 0)
            addToTop(new TriggerDoTAction(target, amount, triggerAll, powerID));
        
        boolean removeOnTrigger;
        AbstractPower r = source.getPower(ReignOfKeysPower.POWER_ID);
        if (r != null) {
            removeOnTrigger = false;
            if (((ReignOfKeysPower)r).upgraded) 
                addToTop(new DrawCardAction(1));
        } else {
            removeOnTrigger = true;
        }

        if (triggerAll) {
            dots.forEach(d -> d.trigger(removeOnTrigger));
        } else {
            dots.get(AbstractDungeon.cardRandomRng.random(dots.size() - 1)).trigger(removeOnTrigger);
        }
    }
}
