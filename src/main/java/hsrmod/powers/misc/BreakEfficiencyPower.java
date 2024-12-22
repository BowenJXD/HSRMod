package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.ISetToughnessReductionSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class BreakEfficiencyPower extends BuffPower implements ISetToughnessReductionSubscriber, PreElementalDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BreakEfficiencyPower.class.getSimpleName());

    public static final float MULTIPLIER = 0.5f;

    public BreakEfficiencyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], (int) (MULTIPLIER * 100));
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this, true);
    }

    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public int setToughnessReduction(AbstractCreature target, int amt) {
        if (SubscriptionManager.checkSubscriber(this))
            return amt + (int) ((float)amt * MULTIPLIER);
        return amt;
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (action.info.card instanceof BaseCard) {
                BaseCard card = (BaseCard) action.info.card;
                if (card.baseTr == card.tr) {
                    action.info.tr = (int) (action.info.tr * (1 + MULTIPLIER));
                }
            }
        }
        return dmg;
    }
}
