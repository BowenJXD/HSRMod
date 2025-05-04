package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class SMR2AmygdalaPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(SMR2AmygdalaPower.class.getSimpleName());
    
    public int minAmount;
    
    public SMR2AmygdalaPower(int minAmount) {
        super(POWER_ID);
        this.minAmount = minAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], minAmount);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        addToBot(new ApplyPowerAction(owner, owner, new BrainInAVatPower(owner, 1), 1));
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        int num = ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID) + stackAmount;
        if (power instanceof EnergyPower 
                && num < minAmount 
                && target.getPower(EnergyPower.POWER_ID) != null 
                && !((EnergyPower)target.getPower(EnergyPower.POWER_ID)).isLocked()) {
            flash();
            int diff = minAmount - num;
            addToTop(new ApplyPowerAction(owner, owner, new EnergyPower(owner, diff), diff));
        }
        return stackAmount;
    }
}
