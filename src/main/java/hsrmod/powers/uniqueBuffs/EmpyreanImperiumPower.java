package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.RandomCardFromDrawPileToHandAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BoostPower;

import java.util.ArrayList;
import java.util.List;

public class EmpyreanImperiumPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(EmpyreanImperiumPower.class.getSimpleName());

    public int threshold = 2;
    
    public EmpyreanImperiumPower(boolean upgraded, int threshold) {
        super(POWER_ID, upgraded);
        this.threshold = threshold;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], threshold);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= threshold) {
            addToTop(new DrawCardAction(1));
            amount -= threshold;
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (!AbstractDungeon.player.hand.isEmpty() 
                && card == AbstractDungeon.player.hand.getBottomCard()) {
            flash();
            stackPower(upgraded && (card.costForTurn > 0 || (card.costForTurn == -1 && card.energyOnUse > 0)) ? 2 : 1 );
        }
    }
}
