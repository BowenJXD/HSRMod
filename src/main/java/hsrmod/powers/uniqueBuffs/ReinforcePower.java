package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.Hsrmod;
import hsrmod.cards.common.March7th2;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.OnPlayerDamagedSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class ReinforcePower extends BuffPower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(ReinforcePower.class.getSimpleName());
    
    public int block = 1;

    public ReinforcePower(AbstractCreature owner, int block) {
        super(POWER_ID, owner);
        this.block = block;
        this.isTurnBased = true;
        this.updateDescription();
    }
    
    @Override
    public void updateDescription() {
        if (!upgraded)
            this.description = String.format(DESCRIPTIONS[0]);
        else
            this.description = String.format(DESCRIPTIONS[1], block);
    }

    @Override
    public void atStartOfTurn() {
        remove(1);
    }

    @Override
    public void onInitialApplication() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this) 
                && AbstractDungeon.player.currentBlock > 0
                && damageInfo.type != DamageInfo.DamageType.HP_LOSS
                && damageInfo.owner instanceof AbstractMonster) {
            if (AbstractDungeon.player.hand.size() < 10) {
                March7th2 card = new March7th2();
                card.priorityTarget = (AbstractMonster) damageInfo.owner;
                addToTop(new MakeTempCardInHandAction(card));
            }
            if (block > 0) {
                addToTop(new GainBlockAction(owner, owner, block));
            }
        }
        return i;
    }
}
