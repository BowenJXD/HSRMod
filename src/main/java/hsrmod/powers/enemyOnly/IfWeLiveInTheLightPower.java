package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;

public class IfWeLiveInTheLightPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(IfWeLiveInTheLightPower.class.getSimpleName());

    int dmgMultiplier = 50;
    
    public IfWeLiveInTheLightPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner);
        isTurnBased = true;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], dmgMultiplier);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (owner.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower toughnessPower = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
            toughnessPower.lock(this);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (owner.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower toughnessPower = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
            toughnessPower.unlock(this);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage * (1 + dmgMultiplier / 100f);
        }
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }
}
