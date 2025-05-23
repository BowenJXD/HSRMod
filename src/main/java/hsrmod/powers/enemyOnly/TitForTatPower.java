package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class TitForTatPower extends BuffPower implements PostBreakBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(TitForTatPower.class.getSimpleName());

    ElementalDamageInfo info;

    public TitForTatPower(AbstractCreature owner, int amount, ElementalDamageInfo damageInfo) {
        super(POWER_ID, owner, amount);
        this.info = damageInfo;
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], info.base, info.tr);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        addToTop(new ApplyPowerAction(owner, owner, new LockToughnessPower(owner)));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        addToTop(new RemoveSpecificPowerAction(owner, owner, LockToughnessPower.POWER_ID));
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this)
                && c == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ElementalDamageAction(owner, info, AbstractGameAction.AttackEffect.NONE));
        }
    }
}
