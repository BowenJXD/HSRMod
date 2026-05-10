package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;

public class ScourgePower extends BuffPower implements PreEnergyChangeSubscriber {
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
        SubscriptionManager.subscribe(this);
        energyCache = EnergyPanel.totalCount;
        energyLimitCache = AbstractDungeon.player.energy.energy;
        EnergyPanel.setEnergy(amount);
        AbstractDungeon.player.energy.energy = STACK_LIMIT;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        EnergyPanel.setEnergy(energyCache);
        AbstractDungeon.player.energy.energy = energyLimitCache;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= STACK_LIMIT) {
            amount = STACK_LIMIT;
        }
        EnergyPanel.setEnergy(amount);
        AbstractDungeon.player.energy.energy = STACK_LIMIT;
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount <= 0) {
            amount = 0;
        }
        EnergyPanel.setEnergy(amount);
        AbstractDungeon.player.energy.energy = STACK_LIMIT;
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (EnergyPanel.totalCount + changeAmount > STACK_LIMIT) {
                changeAmount = STACK_LIMIT - EnergyPanel.totalCount;
            }
            if (EnergyPanel.totalCount + changeAmount < 0) {
                changeAmount = -EnergyPanel.totalCount;
            }
            if (changeAmount > 0) {
                stackPower(changeAmount);
            } else if (changeAmount < 0) {
                reducePower(-changeAmount);
            }
        }
        return changeAmount;
    }
}
