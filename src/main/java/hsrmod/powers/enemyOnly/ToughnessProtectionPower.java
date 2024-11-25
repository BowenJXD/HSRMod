package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.ToughnessPower;

public class ToughnessProtectionPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ToughnessProtectionPower.class.getSimpleName());
    
    public ToughnessProtectionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        ToughnessPower toughness = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
        if (toughness != null) {
            toughness.lock(this);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        ToughnessPower toughness = (ToughnessPower) owner.getPower(ToughnessPower.POWER_ID);
        if (toughness != null) {
            toughness.unlock(this);
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }
}
