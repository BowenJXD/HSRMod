package hsrmod.cardsV2;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class DestinysThreadsForewoven extends BaseCard {
    public static final String ID = DestinysThreadsForewoven.class.getSimpleName();
    
    public DestinysThreadsForewoven() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], cards -> {
            if (!cards.isEmpty()) {
                AbstractCard card = cards.get(0);
                addToTop(new ExhaustSpecificCardAction(card, p.hand));
                addToTop(new GainBlockAction(p, p, block + (card.isInnate ? magicNumber : 0)));
            }
        }));
    }
}
