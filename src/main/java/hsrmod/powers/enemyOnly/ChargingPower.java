package hsrmod.powers.enemyOnly;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ChargingPower extends StatePower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ChargingPower.class.getSimpleName());
    
    public String move;
    
    public ChargingPower(AbstractCreature owner, String move, int amount) {
        super(POWER_ID, owner, amount);
        this.move = move;
        if (amount > 1)
            description = String.format(DESCRIPTIONS[1], move, amount);
        else
            description = String.format(DESCRIPTIONS[0], move);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount > 1)
            description = String.format(DESCRIPTIONS[1], move, amount);
        else
            description = String.format(DESCRIPTIONS[0], move);
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
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        AbstractDungeon.effectList.add(new BetterWarningSignEffect(owner.hb.cX, owner.hb.cY + owner.hb.y, 3.0f));
    }

    @Override
    public void duringTurn() {
        super.duringTurn();
        remove(1);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == this.owner) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            if (owner instanceof AbstractMonster)
                ((AbstractMonster)owner).intent = AbstractMonster.Intent.STUN;
        }
    }
}
