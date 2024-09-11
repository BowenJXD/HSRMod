package hsrmod.powers.uniqueDebuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;

public class ThanatoplumRebloomPower extends DebuffPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(ThanatoplumRebloomPower.class.getSimpleName());

    public ThanatoplumRebloomPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        priority = 7;
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        AbstractPower power = owner.getPower(BrokenPower.POWER_ID);
        if (power != null) {
            ((BrokenPower)power).doReduce = false;
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof ToughnessPower 
                && !abstractCreature.hasPower(ToughnessPower.POWER_ID) 
                && abstractPower.amount > 0) {
            flash();
            reducePower(1);
            return false;
        }
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof ToughnessPower && stackAmount > 0) {
            flash();
            reducePower(1);
            return 0;
        }
        return stackAmount;
    }
}
