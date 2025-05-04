package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.BaseCard;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GalaxyRanger extends BaseCard {
    public static final String ID = GalaxyRanger.class.getSimpleName();
    public static final String[] TEXT;

    public GalaxyRanger() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(p.hand.size(), TEXT[0], 
                true, true, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return true;
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> list) {
                if (list.isEmpty())
                    return;
                GalaxyRanger.this.addToTop(new DrawCardAction(list.size() * 2));
                if (upgraded)
                    for (AbstractCard c : list)
                        if (c.canUpgrade())
                            GalaxyRanger.this.addToTop(new UpgradeSpecificCardAction(c));
                GalaxyRanger.this.addToTop(new DiscardCardsAction(list));
            }
        }));
    }
    
    static {
        TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    }
}
