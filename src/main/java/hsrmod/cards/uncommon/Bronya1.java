package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.MoveCardsAction;
import hsrmod.actions.SelectCardsAction;
import hsrmod.cards.BaseCard;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Bronya1 extends BaseCard {
    public static final String ID = Bronya1.class.getSimpleName();
    
    public Bronya1() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        
        addToBot(new SelectCardsAction(p.drawPile.group, 1, cardStrings.EXTENDED_DESCRIPTION[0], new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> list) {
                if (!list.isEmpty()) {
                    AbstractCard c = list.get(0);
                    AbstractDungeon.actionManager.addToTop(new MoveCardsAction(p.hand, p.drawPile, new Predicate<AbstractCard>() {
                        @Override
                        public boolean test(AbstractCard card) {
                            return card == c;
                        }
                    }, 1));
                }
            }
        }));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.exhaust = this.exhaust;
        return card;
    }
}
