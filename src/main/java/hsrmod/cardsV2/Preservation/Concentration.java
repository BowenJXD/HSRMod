package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.GridCardManipulator;
import hsrmod.actions.MoveCardsAction;
import hsrmod.actions.SimpleGridCardSelectBuilder;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Concentration extends BaseCard {
    public static final String ID = Concentration.class.getSimpleName();

    public Concentration() {
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
                        if (hasBlock && m.currentBlock <= 0) {
                            if (upgraded) {
                                Concentration.this.addToBot(new SimpleGridCardSelectBuilder(new Predicate<AbstractCard>() {
                                            @Override
                                            public boolean test(AbstractCard c) {
                                                return c.isInnate;
                                            }
                                        })
                                                .setCardGroup(p.drawPile, p.discardPile, p.exhaustPile)
                                                .setAmount(magicNumber)
                                                .setAnyNumber(true)
                                                .setCanCancel(true)
                                                .setMsg(cardStrings.EXTENDED_DESCRIPTION[0])
                                                .setManipulator(new GridCardManipulator() {
                                                    @Override
                                                    public boolean manipulate(AbstractCard card, int index, CardGroup group) {
                                                        moveToHand(card, group);
                                                        return false;
                                                    }
                                                })
                                );
                            } else {
                                long limit = magicNumber;
                                for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
                                    @Override
                                    public boolean test(AbstractCard card) {
                                        return card.isInnate;
                                    }
                                }, true)) {
                                    if (r.group != AbstractDungeon.player.hand) {
                                        if (limit-- == 0) break;
                                        Concentration.this.addToTop(new MoveCardsAction(AbstractDungeon.player.hand, r.group, new Predicate<AbstractCard>() {
                                            @Override
                                            public boolean test(AbstractCard card) {
                                                return card == r.card;
                                            }
                                        }, 1));
                                    }
                                }
                            }
                        }
                    }
                })
        );
    }
}
