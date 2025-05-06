package androidTestMod.powers.uniqueBuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class MatrixOfPresciencePower extends BuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(MatrixOfPresciencePower.class.getSimpleName());
    
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
