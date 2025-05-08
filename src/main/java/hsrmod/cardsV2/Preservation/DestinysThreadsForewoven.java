package hsrmod.cardsV2.Preservation;

import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DestinysThreadsForewoven extends BaseCard {
    public static final String ID = DestinysThreadsForewoven.class.getSimpleName();
    
    public DestinysThreadsForewoven() {
        super(ID);
        setBaseEnergyCost(80);
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(magicNumber, cardStrings.EXTENDED_DESCRIPTION[0], true, true, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                return true;
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> cards) {
                if (!cards.isEmpty()) {
                    for (AbstractCard card : cards) {
                        DestinysThreadsForewoven.this.addToTop(new ExhaustSpecificCardAction(card, p.hand));
                    }
                    if (m != null) DestinysThreadsForewoven.this.addToTop(new GainBlockAction(m, m, cards.size()));
                    DestinysThreadsForewoven.this.addToTop(new GainBlockAction(p, p, block * cards.size()));
                }
            }
        }));
    }
}
