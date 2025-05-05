package androidTestMod.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * @author Kurrent Gayronld (Somdy)
 * @see <a href="https://github.com/Somdy/LazyManKits">LazyManKits</a>
 */
public abstract class GridCardManipulator {
    /**
     * do something to the card chosen
     * @param card the card player chooses
     * @param index the index of the card in the list chosen, usually starts at 0
     * @param group the group where the card is
     * @return useless in grid card selection currently, whatever you want
     */
    public abstract boolean manipulate(AbstractCard card, int index, CardGroup group);

    public void moveToHand(AbstractCard card, CardGroup from) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hand.size() < 10) {
            setCardPosition(card, from);
            card.lighten(false);
            card.unhover();
            card.applyPowers();
            p.hand.addToHand(card);
            from.removeCard(card);
            p.hand.refreshHandLayout();
        }
    }

    public void moveToDrawPile(AbstractCard card, boolean onTop) {
        AbstractPlayer p = AbstractDungeon.player;
        p.drawPile.moveToDeck(card, !onTop);
    }

    private void setCardPosition(AbstractCard card, CardGroup from) {
        switch (from.type) {
            case DRAW_PILE:
                card.target_x = CardGroup.DRAW_PILE_X;
                card.target_y = CardGroup.DRAW_PILE_Y;
                break;
            case DISCARD_PILE:
                card.target_x = CardGroup.DISCARD_PILE_X;
                card.target_y = CardGroup.DISCARD_PILE_Y;
                break;
        }
    }
}
