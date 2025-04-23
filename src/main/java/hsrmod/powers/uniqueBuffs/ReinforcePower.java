package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.common.March7th1;
import hsrmod.cards.common.March7th2;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.SubscriptionManager;

import java.util.Objects;

public class ReinforcePower extends BuffPower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ReinforcePower.class.getSimpleName());
    
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
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
        if (!SignatureHelper.isUnlocked(HSRMod.makePath(March7th1.ID))) {
            int count = AbstractDungeon.player.hand.group.stream().mapToInt(c -> Objects.equals(c.cardID, HSRMod.makePath(March7th2.ID)) ? 1 : 0).sum();
            if (count == 7) {
                SignatureHelper.unlock(HSRMod.makePath(March7th1.ID), true);
            }
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this) 
                && AbstractDungeon.player.currentBlock > 0
                && damageInfo.type != DamageInfo.DamageType.HP_LOSS
                && damageInfo.owner instanceof AbstractMonster) {
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
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
