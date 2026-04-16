package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;

public class ScourgePower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ScourgePower.class.getSimpleName());
    
    int energyCache = 0;
    int energyLimitCache = 0;
    static final int STACK_LIMIT = 7;
    
    public ScourgePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], STACK_LIMIT);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        energyCache = EnergyPanel.totalCount;
        energyLimitCache = AbstractDungeon.player.energy.energy;
        EnergyPanel.setEnergy(amount);
        AbstractDungeon.player.energy.energy = STACK_LIMIT;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        EnergyPanel.setEnergy(energyCache);
        AbstractDungeon.player.energy.energy = energyLimitCache;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= STACK_LIMIT) {
            amount = STACK_LIMIT;
        }
    }
}
