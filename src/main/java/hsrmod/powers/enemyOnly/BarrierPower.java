package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class BarrierPower extends BuffPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BarrierPower.class.getSimpleName());
    
    public BarrierPower(AbstractMonster owner, int amount) {
        super(POWER_ID, owner, amount);
        priority = 3;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
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
    public void atStartOfTurn() {
        super.atStartOfTurn();
        reducePower(1);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS) {
            reducePower(1);
            return 0;
        }
        return damageAmount;
    }

    @Override
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == owner
                && amount > 0) {
            return 0;
        }
        return amount;
    }
}
