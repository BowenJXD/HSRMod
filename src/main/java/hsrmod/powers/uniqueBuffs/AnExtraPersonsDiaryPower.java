package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.misc.ToughnessPower;

import java.util.ArrayList;
import java.util.List;

public class AnExtraPersonsDiaryPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(AnExtraPersonsDiaryPower.class.getSimpleName());
    
    public List<DoTPower> changedPowers;
    
    public AnExtraPersonsDiaryPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        changedPowers = new ArrayList<>();
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        changePowers();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        for (DoTPower p : changedPowers) {
            p.toughnessReduction -= 1;
        }
    }

    void changePowers() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof DoTPower && !changedPowers.contains(p)) {
                    DoTPower power = (DoTPower) p;
                    power.toughnessReduction += 1;
                    changedPowers.add(power);
                }
            }
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (power instanceof DoTPower && !changedPowers.contains(power)) {
            DoTPower doTPower = (DoTPower) power;
            ((DoTPower) power).toughnessReduction += 1;
            changedPowers.add(doTPower);
        }
        else if (power instanceof BrokenPower) {
            int x = ToughnessPower.getStackLimit(target) / 10 + 1;
            for (int i = 0; i < x; i++) {
                addToBot(new ApplyPowerAction(target, owner, DoTPower.getRandomDoTPower(target, source, 1), 1));
            }
            if (upgraded)
                addToBot(new GainEnergyAction(1));
        }
    }
}
