package hsrmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class RandomCardFromDrawPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;

    public Consumer<AbstractCard> callback;

    public RandomCardFromDrawPileToHandAction(Consumer<AbstractCard> callback) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.callback = callback;
    }

    public RandomCardFromDrawPileToHandAction() {
        this(null);
    }

    public void update() {
        if (!this.p.drawPile.isEmpty()) {
            if (this.p.hand.size() >= BaseMod.MAX_HAND_SIZE) 
                p.createHandIsFullDialog();
            else {
                AbstractCard card = this.p.drawPile.getRandomCard(AbstractDungeon.cardRandomRng);
                this.p.hand.addToHand(card);
                card.lighten(false);
                this.p.drawPile.removeCard(card);
                this.p.hand.refreshHandLayout();

                if (callback != null) callback.accept(card);
            }
        }

        this.tickDuration();
        this.isDone = true;
    }
}
