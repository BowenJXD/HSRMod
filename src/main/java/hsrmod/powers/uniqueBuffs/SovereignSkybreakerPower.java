package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BoostPower;
import hsrmod.utils.ModHelper;

public class SovereignSkybreakerPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(SovereignSkybreakerPower.class.getSimpleName());

    public int triggerAmount = 5;
    
    public SovereignSkybreakerPower(int triggerAmount, boolean upgraded) {
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
            AbstractCard card = ModHelper.getRandomElement(AbstractDungeon.player.hand.group, AbstractDungeon.cardRandomRng, c -> c.costForTurn > 0);
            if (card != null) addToTop(new ReduceCostForTurnAction(card, 1));
            addToTop(new ApplyPowerAction(owner, owner, new BoostPower(owner, 2), 2));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (card.type == AbstractCard.CardType.SKILL) {
            stackPower(1);
        }
        if (card.type != AbstractCard.CardType.POWER && (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ENEMY)) {
            stackPower(1);
        }
        if (upgraded && card.upgraded) {
            stackPower(1);
        }
    }
}
