package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;

public class DestructionLotusPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(DestructionLotusPower.class.getSimpleName());
    
    public DestructionLotusPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        updateDescription();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        AbstractPower power = AbstractDungeon.player.getPower(DrawReductionPower.POWER_ID);
        if (power != null) {
            addToTop(new DrawCardAction(power.amount * 2));
            addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
        }
    }
}
