package hsrmod.cardsV2.Propagation;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.MoveCardsAction;
import hsrmod.actions.SelectCardsAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Sparkle3 extends BaseCard {
    public static final String ID = Sparkle3.class.getSimpleName();
    
    public Sparkle3() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));

        addToBot(new SelectCardsAction(p.discardPile.group, 1, cardStrings.EXTENDED_DESCRIPTION[1], new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> list) {
                if (!list.isEmpty()) {
                    AbstractCard c = list.get(0);
                    AbstractDungeon.actionManager.addToTop(new MoveCardsAction(p.hand, p.discardPile, new Predicate<AbstractCard>() {
                        @Override
                        public boolean test(AbstractCard card) {
                            return card == c;
                        }
                    }));
                } else {
                    SignatureHelper.unlock(HSRMod.makePath(ID), true);
                }
            }
        }));

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                List<ModHelper.FindResult> sparkles = ModHelper.findCards(new Predicate<AbstractCard>() {
                    @Override
                    public boolean test(AbstractCard c) {
                        return c instanceof Sparkle3 && c.uuid != uuid;
                    }
                });
                if (!sparkles.isEmpty()) {
                    Sparkle3.this.addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
                    SignatureHelper.unlock(HSRMod.makePath(ID), true);
                }
                for (ModHelper.FindResult result : sparkles) {
                    result.group.removeCard(result.card);
                    for (AbstractCard abstractCard : p.masterDeck.group) {
                        if (abstractCard.uuid == result.card.uuid) {
                            p.masterDeck.removeCard(abstractCard);
                            break;
                        }
                    }
                }
            }
        });
    }
}
