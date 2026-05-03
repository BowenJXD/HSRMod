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

public class DisputationModePower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(DisputationModePower.class.getSimpleName());

    int evolutionCount;
    int disputationCount = 3;

    public DisputationModePower(AbstractCreature owner, int amount, int evolutionCount) {
        super(POWER_ID, owner, amount);
        disputationCount = amount;
        this.evolutionCount = evolutionCount;
        priority = 1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], 2, 1, evolutionCount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new ApplyPowerAction(owner, owner, new StrengthenPower(owner, 1)));
    }
}
