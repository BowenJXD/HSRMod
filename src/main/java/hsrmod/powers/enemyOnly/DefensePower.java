package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class DefensePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(DefensePower.class.getSimpleName());
    
    int stackLimit = 100;

    public DefensePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        if (amount < 0) {
            type = PowerType.DEBUFF;
        }
        this.isTurnBased = true;
        canGoNegative = true;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount < 0) {
            description = String.format(DESCRIPTIONS[1], -amount);
        } else {
            description = String.format(DESCRIPTIONS[0], amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > stackLimit) {
            amount = stackLimit;
        }
        type = amount < 0 ? PowerType.DEBUFF : PowerType.BUFF;
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount < -stackLimit) {
            amount = -stackLimit;
        }
        type = amount < 0 ? PowerType.DEBUFF : PowerType.BUFF;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            damage = damage * (100 - amount) / 100;
        }
        return damage;
    }
}
