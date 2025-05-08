package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.Hsrmod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ExcitatoryGlandPower extends PowerPower implements PreEnergyChangeSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(ExcitatoryGlandPower.class.getSimpleName());
    
    public boolean canTrigger = false;
    
    public ExcitatoryGlandPower() {
        super(POWER_ID);
        this.updateDescription();
    }
    
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        ++AbstractDungeon.player.energy.energy;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        --AbstractDungeon.player.energy.energy;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        canTrigger = true;
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this)
                && EnergyPanel.getCurrentEnergy() == -changeAmount
                && canTrigger) {
            addToTop(new GainEnergyAction(1));
            canTrigger = false;
        }
        return changeAmount;
    }
}
