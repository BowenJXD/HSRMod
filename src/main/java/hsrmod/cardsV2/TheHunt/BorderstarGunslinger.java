package hsrmod.cardsV2.TheHunt;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class BorderstarGunslinger extends BaseCard {
    public static final String ID = BorderstarGunslinger.class.getSimpleName();

    public BorderstarGunslinger() {
        super(ID);
        energyCost = 80;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(p.hand.size(), CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT[0],
                true, true, c -> true, list -> {
            if (list.isEmpty())
                return;
            addToTop(new GainBlockAction(p, p, list.size() * 2));
            for (AbstractCard c : list) {
                if (upgraded && c.canUpgrade()) addToTop(new UpgradeSpecificCardAction(c));
            }
            addToTop(new DiscardCardsAction(list));
        }));
    }
}
