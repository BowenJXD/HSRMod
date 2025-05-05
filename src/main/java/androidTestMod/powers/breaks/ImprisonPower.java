package androidTestMod.powers.breaks;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.DebuffPower;

public class ImprisonPower extends DebuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(ImprisonPower.class.getSimpleName());

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
