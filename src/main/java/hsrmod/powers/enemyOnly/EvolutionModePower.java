package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;

public class EvolutionModePower extends StatePower implements PostMonsterDeathSubscriber {
    public static final String POWER_ID = HSRMod.makePath(EvolutionModePower.class.getSimpleName());
    
    int monsterCount = 1;
    int evolutionCount;
    int disputationCount = 3;
    
    public EvolutionModePower(AbstractCreature owner, int amount, int disputationCount) {
        super(POWER_ID, owner, amount);
        evolutionCount = amount;
        this.disputationCount = disputationCount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], disputationCount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount == 0) {
            addToTop(new ApplyPowerAction(owner, owner, new DisputationModePower(owner, disputationCount, evolutionCount)));
            addToTop(new PressEndTurnButtonAction());
        }
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (SubscriptionManager.checkSubscriber(this)) {
            this.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
