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

    public EmpyreanImperiumPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (!AbstractDungeon.player.hand.isEmpty() 
                && card == AbstractDungeon.player.hand.getBottomCard()) {
            flash();
            addToTop(new RandomCardFromDrawPileToHandAction(upgraded ? c -> {
                if (c.canUpgrade())
                    addToTop(new UpgradeSpecificCardAction(c));
            } : null));
        }
    }
}
