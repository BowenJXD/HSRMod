package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class BoostPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(BoostPower.class.getSimpleName());
    
    public int stackLimit = 8;
    
    public BoostPower(AbstractCreature c, int amount) {
        super(POWER_ID, c, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], stackLimit, amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > stackLimit) {
            amount = stackLimit;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float)this.amount : damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return damageAmount;
    }
}
