package hsrmod.cardsV2.Erudition;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class PenMan extends BaseCard {
    public static final String ID = PenMan.class.getSimpleName();
    
    public PenMan() {
        super(ID);
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!p.hand.isEmpty() || upgraded) {
            addToBot(new SelectCardsInHandAction(1, String.format(cardStrings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], magicNumber),
                    false, upgraded, c -> true, list -> {
                addToTop(new ApplyPowerAction(p, p, new EnergyPower(p, magicNumber), magicNumber));
                if (list.isEmpty()) {
                    addToTop(new LoseEnergyAction(1));
                } else {
                    addToTop(new DiscardSpecificCardAction(list.get(0)));
                }
            }));
        }
    }
}
