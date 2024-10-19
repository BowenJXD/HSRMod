package hsrmod.cardsV2.TheHunt;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.cards.BaseCard;

public class GalaxyRanger extends BaseCard {
    public static final String ID = GalaxyRanger.class.getSimpleName();
    public static final String[] TEXT;

    public GalaxyRanger() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(p.hand.size(), TEXT[0], 
                true, true, c -> true, list -> {
            if (list.isEmpty())
                return;
            addToTop(new DrawCardAction(list.size() * 2));
            for (AbstractCard c : list) {
                if (upgraded && c.canUpgrade()) addToTop(new UpgradeSpecificCardAction(c));
            }
            addToTop(new DiscardCardsAction(list));
        }));
    }
    
    static {
        TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    }
}
