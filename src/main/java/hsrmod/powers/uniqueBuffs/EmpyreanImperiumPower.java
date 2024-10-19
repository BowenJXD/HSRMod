package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BoostPower;

import java.util.ArrayList;
import java.util.List;

public class EmpyreanImperiumPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(EmpyreanImperiumPower.class.getSimpleName());

    public EmpyreanImperiumPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (!AbstractDungeon.player.hand.isEmpty() 
                && card == AbstractDungeon.player.hand.getTopCard()) {
            flash();
            addToTop(new DrawCardAction(1, upgraded ? new AbstractGameAction() {
                @Override
                public void update() {
                    addToTop(new UpgradeSpecificCardAction(AbstractDungeon.player.hand.getTopCard()));
                    isDone = true;
                }
            } : null));
        }
    }
    
    /*@Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (!actions.contains(AbstractDungeon.actionManager.currentAction)) {
            actions = new ArrayList<>(AbstractDungeon.actionManager.actions);
            actions.add(0, AbstractDungeon.actionManager.currentAction);
            hasUpgradedCard = false;
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new BoostPower(owner, 1), 1));
        }
        if (upgraded && card.upgraded && !hasUpgradedCard) {
            hasUpgradedCard = true;
            addToBot(new ApplyPowerAction(owner, owner, new BoostPower(owner, 1), 1));
        }
    }*/
}
