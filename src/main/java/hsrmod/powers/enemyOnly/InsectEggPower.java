package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class InsectEggPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(InsectEggPower.class.getSimpleName());

    int multiplier = 3;
    
    public InsectEggPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], multiplier, amount, amount * multiplier);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.THORNS) {
            return damage + amount * multiplier;
        }
        return damage;
    }
}
