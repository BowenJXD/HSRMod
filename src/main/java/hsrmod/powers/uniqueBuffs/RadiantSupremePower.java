package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BoostPower;

public class RadiantSupremePower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(RadiantSupremePower.class.getSimpleName());

    public int triggerAmount = 5;
    
    public RadiantSupremePower(int triggerAmount, boolean upgraded) {
        super(POWER_ID, upgraded);
        this.triggerAmount = triggerAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], triggerAmount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= triggerAmount) {
            flash();
            amount -= triggerAmount;
            addToTop(new DrawCardAction(1));
            addToTop(new ApplyPowerAction(owner, owner, new BoostPower(owner, 1), 1));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (card.type == AbstractCard.CardType.ATTACK && action.target != null) {
            stackPower(card.upgraded ? 2 : 1);
        }
    }
}
