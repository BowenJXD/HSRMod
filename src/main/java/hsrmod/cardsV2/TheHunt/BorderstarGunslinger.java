package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BorderstarGunslinger extends BaseCard {
    public static final String ID = BorderstarGunslinger.class.getSimpleName();

    public BorderstarGunslinger() {
        super(ID);
        setBaseEnergyCost(80);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(p.hand.size(), CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT[0],
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
                BorderstarGunslinger.this.addToTop(new GainBlockAction(p, p, list.size() * magicNumber));
                for (AbstractCard c : list) {
                    if (upgraded && c.canUpgrade())
                        BorderstarGunslinger.this.addToTop(new UpgradeSpecificCardAction(c));
                }
                BorderstarGunslinger.this.addToTop(new DiscardCardsAction(list));
            }
        }));
    }
}
