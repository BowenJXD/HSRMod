package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.powers.PowerPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EmpyreanImperiumPower extends PowerPower {
    public static final String POWER_ID = Hsrmod.makePath(EmpyreanImperiumPower.class.getSimpleName());

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
            amount = 0;
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
