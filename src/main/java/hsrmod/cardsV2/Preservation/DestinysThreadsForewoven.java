package hsrmod.cardsV2.Preservation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class DestinysThreadsForewoven extends BaseCard {
    public static final String ID = DestinysThreadsForewoven.class.getSimpleName();
    
    public DestinysThreadsForewoven() {
        super(ID);
        energyCost = 80;
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(magicNumber, cardStrings.EXTENDED_DESCRIPTION[0], true, true, card -> true, cards -> {
            if (!cards.isEmpty()) {
                for (AbstractCard card : cards) {
                    addToTop(new ExhaustSpecificCardAction(card, p.hand));
                }
                addToTop(new GainBlockAction(p, p, block * cards.size()));
            }
        }));
    }
}
