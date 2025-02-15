package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;

public class SimmerPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(SimmerPower.class.getSimpleName());
    
    int stackLimit = 9;

    public SimmerPower(AbstractCreature owner, int stackLimit) {
        super(POWER_ID, owner, 0);
        this.stackLimit = stackLimit;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], stackLimit);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount == stackLimit) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new VFXAction(new ScreenOnFireEffect()));
            addToBot(new ApplyPowerAction(owner, owner, new BoilPower(owner, amount)));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL)
            addToBot(new ApplyPowerAction(owner, owner, new SimmerPower(owner, 1), 1));
        return damageAmount;
    }
}
