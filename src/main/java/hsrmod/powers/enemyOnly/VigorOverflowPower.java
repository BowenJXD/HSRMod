package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;

public class VigorOverflowPower extends StatePower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(VigorOverflowPower.class.getSimpleName());
    
    public VigorOverflowPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof ToughnessPower || abstractPower instanceof StrengthPower) return true;
        flash();
        if (abstractPower.type == PowerType.BUFF) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
        } else if (abstractPower.type == PowerType.DEBUFF) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -amount)));
        }
        return true;
    }
}
