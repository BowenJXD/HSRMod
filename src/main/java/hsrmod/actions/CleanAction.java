package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.powers.misc.ToughnessPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class CleanAction extends AbstractGameAction {
    boolean removeAll;
    Predicate<AbstractPower> filter;

    public CleanAction(AbstractCreature target, int amount, boolean removeAll, Predicate<AbstractPower> filter) {
        this.target = target;
        this.amount = amount;
        this.removeAll = removeAll;
        this.filter = filter;
    }

    public CleanAction(AbstractCreature target, int amount, boolean removeAll) {
        this(target, amount, removeAll, new Predicate<AbstractPower>() {
            @Override
            public boolean test(AbstractPower p) {
                return p.type == AbstractPower.PowerType.DEBUFF && !(p instanceof ToughnessPower);
            }
        });
    }

    @Override
    public void update() {
        isDone = true;
        ArrayList<AbstractPower> abstractPowers = new ArrayList<>();
        Predicate<AbstractPower> predicate = filter;
        for (AbstractPower abstractPower : target.powers) {
            if (predicate.test(abstractPower)) {
                abstractPowers.add(abstractPower);
            }
        }
        List<AbstractPower> powers = abstractPowers;
        Collections.shuffle(powers, new Random(AbstractDungeon.cardRandomRng.randomLong()));
        for (AbstractPower power : powers) {
            int removeAmount = removeAll ? 1 : Math.min(amount, power.amount);

            if (removeAll || power.amount == removeAmount) {
                addToTop(new RemoveSpecificPowerAction(target, target, power));
            } else {
                addToTop(new ReducePowerAction(target, target, power, removeAmount));
            }

            amount -= removeAmount;
            if (amount <= 0) break;
        }
    }
}
