package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class MatrixOfPresciencePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(MatrixOfPresciencePower.class.getSimpleName());
    
    float damageReduction = 1 / 2f;
    
    public MatrixOfPresciencePower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageReduction * 100));
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return damage * ( 1 - damageReduction);
    }
}
