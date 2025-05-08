package hsrmod.cardsV2.Preservation;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Burst extends BaseCard {
    public static final String ID = Burst.class.getSimpleName();

    public Burst() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlock = m.currentBlock > 0;
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (hasBlock && ci.target.currentBlock <= 0) {
                            if (upgraded) {
                                Burst.this.addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], new Predicate<AbstractCard>() {
                                    @Override
                                    public boolean test(AbstractCard c) {
                                        return c.isInnate;
                                    }
                                }, new Consumer<List<AbstractCard>>() {
                                    @Override
                                    public void accept(List<AbstractCard> list) {
                                        for (AbstractCard c : list) {
                                            Burst.this.addToTop(new ReduceCostForTurnAction(c, c.costForTurn));
                                        }
                                    }
                                }));
                            } else {
                                for (AbstractCard abstractCard : p.hand.group) {
                                    if (abstractCard.isInnate) {
                                        Burst.this.addToBot(new ReduceCostForTurnAction(abstractCard, abstractCard.costForTurn));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
        );
    }
}
