package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class SoulGladRevelPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(SoulGladRevelPower.class.getSimpleName());
    
    int stackLimit = 5;
    
    public SoulGladRevelPower(AbstractCreature owner, int amount, int stackLimit) {
        super(POWER_ID, owner, amount);
        this.stackLimit = stackLimit;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount, stackLimit);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS) {
            stackPower(1);
        }
        return damageAmount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage + amount;
        }
        return damage;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > stackLimit) {
            amount = stackLimit;
        }
    }
}
