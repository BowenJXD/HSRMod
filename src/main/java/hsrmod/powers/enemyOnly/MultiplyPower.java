package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class MultiplyPower extends StatePower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(MultiplyPower.class.getSimpleName());
    
    public MultiplyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
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
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new ApplyPowerAction(owner, owner, new InsectEggPower(owner, amount)));
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new RemoveSpecificPowerAction(owner, owner, InsectEggPower.POWER_ID));
            addToBot(new AOEAction(m -> new ApplyPowerAction(m, m, new VulnerablePower(m, 1, true))));
            addToBot(new AOEAction(m -> new ApplyPowerAction(m, m, new StunMonsterPower(m))));
        }
    }
}
