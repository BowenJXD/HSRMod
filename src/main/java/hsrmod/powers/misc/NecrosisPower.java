package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class NecrosisPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(NecrosisPower.class.getSimpleName());
    
    public NecrosisPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        remove(1);
        addToBot(new TriggerPowerAction(this));
    }

    @Override
    public void onSpecificTrigger() {
        if (amount <= 0) return;
        super.onSpecificTrigger();
        if (TempHPField.tempHp.get(owner) >= amount) {
            addToBot(new LoseHPAction(owner, owner, amount));
        }
    }
}
