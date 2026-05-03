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

public class EvolutionModePower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(EvolutionModePower.class.getSimpleName());
    
    int evolutionCount;
    int disputationCount = 3;
    
    public EvolutionModePower(AbstractCreature owner, int amount, int disputationCount) {
        super(POWER_ID, owner, amount);
        evolutionCount = amount;
        this.disputationCount = disputationCount;
        priority = 1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], 1, disputationCount);
    }
}
