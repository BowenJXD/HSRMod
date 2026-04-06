package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class LoseFocusPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(LoseFocusPower.class.getSimpleName());
    
    public LoseFocusPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new ReducePowerAction(owner, owner, FocusPower.POWER_ID, amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }
}
