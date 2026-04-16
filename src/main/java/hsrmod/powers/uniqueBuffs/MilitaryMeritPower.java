package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.PowerPower;

public class MilitaryMeritPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(MilitaryMeritPower.class.getSimpleName());
    
    final int stackLimit = 8;
    final int triggerAmount = 6;
    int removeAmount = 6;
    int vigorAmount;
    
    public MilitaryMeritPower(AbstractCreature target, int Amount, int vigorAmount) {
        super(POWER_ID, target, Amount);
        this.vigorAmount = vigorAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], triggerAmount, removeAmount, vigorAmount);
    }
    
    @Override
    public void stackPower(int stackAmount) {
        boolean triggered = amount >= triggerAmount;
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
        }
        if (!triggered && this.amount >= triggerAmount) {
            addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, vigorAmount), vigorAmount));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK) {
            if (this.amount >= triggerAmount && !(card.costForTurn == 0 || card.energyOnUse == 0 || card.freeToPlayOnce)) {
                this.flash();

                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                reducePower(removeAmount);
            } else {
                stackPower(1);
            }
        }
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        removeAmount = 5;
    }
}
