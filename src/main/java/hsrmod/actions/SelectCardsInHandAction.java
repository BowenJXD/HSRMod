package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectCardsInHandAction extends AbstractGameAction {
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;
    private String text;
    private boolean anyNumber;
    private boolean canPickZero;
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;

    public SelectCardsInHandAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this.tempHand = new ArrayList();
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.text = textForSelect;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.predicate = cardFilter;
        this.callback = callback;
        this.hand = AbstractDungeon.player.hand.group;
    }

    public SelectCardsInHandAction(int amount, String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, cardFilter, callback);
    }

    public SelectCardsInHandAction(int amount, String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return true;
            }
        }, callback);
    }

    public SelectCardsInHandAction(String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, cardFilter, callback);
    }

    public SelectCardsInHandAction(String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return true;
            }
        }, callback);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.callback == null) {
                this.isDone = true;
            } else {
                this.hand.removeIf(new Predicate<AbstractCard>() {
                    @Override
                    public boolean test(AbstractCard c) {
                        return !SelectCardsInHandAction.this.predicate.test(c) && SelectCardsInHandAction.this.tempHand.add(c);
                    }
                });
                if (this.hand.size() == 0) {
                    this.finish();
                } else if (this.hand.size() <= this.amount && !this.anyNumber && !this.canPickZero) {
                    ArrayList<AbstractCard> spoof = new ArrayList(this.hand);
                    this.hand.clear();
                    this.callback.accept(spoof);
                    this.hand.addAll(spoof);
                    this.finish();
                } else {
                    AbstractDungeon.handCardSelectScreen.open(this.text, this.amount, this.anyNumber, this.canPickZero);
                    this.tickDuration();
                }
            }
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            this.callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            this.hand.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.finish();
        } else {
            this.tickDuration();
        }
    }

    private void finish() {
        this.hand.addAll(this.tempHand);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        this.isDone = true;
    }
}

