package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class IratePower extends StatePower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(IratePower.class.getSimpleName());

    public static int stackLimit = 16;
    
    boolean justApplied = true;
    
    public IratePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }
    
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], stackLimit);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        justApplied = false;
    }

    @Override
    public void duringTurn() {
        super.duringTurn();
        if (amount == stackLimit && !justApplied) {
            triggerMonster();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (this.amount == stackLimit) return;
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
            if (AbstractDungeon.actionManager.monsterQueue.stream().noneMatch(q -> q.monster == owner) && !justApplied) {
                if (owner instanceof AbstractMonster) {
                    ModHelper.addToTopAbstract(() -> ((AbstractMonster) owner).createIntent());
                    addToTop(new RollMoveAction((AbstractMonster) owner));
                }
                triggerMonster();
            }
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (this.amount <= 0) {
            this.amount = 0;
            flash();
            addToBot(new TalkAction(owner, DESCRIPTIONS[2], 2.0F, 3.0F));
            if (!AbstractDungeon.actionManager.turnHasEnded)
                addToBot(new EndTurnAction());
            addToBot(new SkipEnemiesTurnAction());
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this)
                && damageInfo.type == DamageInfo.DamageType.NORMAL
                && i > 0
                && AbstractDungeon.actionManager.turnHasEnded) {
            stackPower(1);
        }
        return i;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL
                && damageAmount > 0) {
            reducePower(1);
        }
        return damageAmount;
    }
    
    void triggerMonster(){
        flash();
        addToBot(new TalkAction(owner, DESCRIPTIONS[1], 2.0F, 3.0F));
        if (!AbstractDungeon.actionManager.turnHasEnded)
            addToBot(new EndTurnAction());
        AbstractDungeon.getCurrRoom().monsters.queueMonsters();
    }
}
