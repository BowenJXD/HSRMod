package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.common.March7th2;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.SubscribeManager;

public class ReinforcePower extends BuffPower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ReinforcePower.class.getSimpleName());
    
    private int block = 1;

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
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscribeManager.checkSubscriber(this) 
                && AbstractDungeon.player.currentBlock > 0
                && damageInfo.type != DamageInfo.DamageType.HP_LOSS) {
            AbstractCard card = new March7th2();
            addToBot(new MakeTempCardInHandAction(card));
            if (block > 0) {
                addToTop(new GainBlockAction(owner, owner, block));
            }
        }
        return i;
    }
}
