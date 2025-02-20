package hsrmod.powers.breaks;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class ImprisonPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(ImprisonPower.class.getSimpleName());

    private final float damageDecrementPercentage = 1f / 3f;

    public ImprisonPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageDecrementPercentage * 100));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        remove(1);
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1 - damageDecrementPercentage);
        } else {
            return damage;
        }
    }
}
