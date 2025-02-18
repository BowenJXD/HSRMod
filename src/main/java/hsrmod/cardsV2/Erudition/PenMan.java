package hsrmod.cardsV2.Erudition;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class PenMan extends BaseCard {
    public static final String ID = PenMan.class.getSimpleName();
    
    public PenMan() {
        super(ID);
        selfRetain = true;
        returnToHand = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        if (!p.hand.isEmpty() || upgraded) {
            addToBot(new SelectCardsInHandAction(1, String.format(cardStrings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], magicNumber),
                    false, upgraded, c -> true, list -> {
                ModHelper.addToTopAbstract(() -> returnToHand = ModHelper.getPowerCount(p, EnergyPower.POWER_ID) < EnergyPower.AMOUNT_LIMIT);
                if (list.isEmpty() && energyOnUse > 0) {
                    addToTop(new ApplyPowerAction(p, p, new EnergyPower(p, magicNumber * 2), magicNumber * 2));
                    addToTop(new LoseEnergyAction(1));
                } else if (!list.isEmpty()){
                    addToTop(new ApplyPowerAction(p, p, new EnergyPower(p, magicNumber), magicNumber));
                    addToTop(new DiscardSpecificCardAction(list.get(0)));
                }
            }));
        }
    }
}
