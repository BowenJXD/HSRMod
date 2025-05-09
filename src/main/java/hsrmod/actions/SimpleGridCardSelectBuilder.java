package hsrmod.actions;

import basemod.BaseMod;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.subscribers.SubscriptionManager;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Kurrent Gayronld (Somdy)
 * @see <a href="https://github.com/Somdy/LazyManKitss">LazyManKits</a>
 */
public class SimpleGridCardSelectBuilder extends AbstractGameAction implements PostDungeonUpdateSubscriber {
    private String msg;
    private GridCardManipulator cm;
    @Deprecated
    private boolean shouldMatchAll;
    private CardGroup[] cardGroups;
    private CardGroup tmpGroup;
    private Predicate<AbstractCard> predicate;
    private boolean anyNumber;
    private boolean canCancel;
    private boolean forUpgrade;
    private boolean forTransform;
    private boolean forPurge;
    private boolean displayInOrder;
    private boolean stopGlowing;
    private boolean gridOpened;
    Map<AbstractCard, CardGroup> cardGroupMap = new HashMap<>();

    @SafeVarargs
    @Deprecated
    public SimpleGridCardSelectBuilder(String msg, GridCardManipulator cm, boolean shouldMatchAll, boolean anyNumber, boolean canCancel,
                                       boolean forUpgrade, boolean forTransform, boolean forPurge, Predicate<AbstractCard>... predicate) {
        this.msg = msg;
        this.cm = cm;
        this.shouldMatchAll = shouldMatchAll;
        this.anyNumber = anyNumber;
        this.canCancel = canCancel;
        this.forUpgrade = forUpgrade;
        this.forTransform = forTransform;
        this.forPurge = forPurge;
        this.predicate = predicate[0];
        if (predicate.length > 1) {
            for (int i = 1; i < predicate.length; i++) {
                this.predicate = this.predicate.or(predicate[i]);
            }
        }
        tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        displayInOrder = false;
        stopGlowing = true;
        gridOpened = false;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        BaseMod.subscribe(this);
    }

    public SimpleGridCardSelectBuilder(String msg, GridCardManipulator cm, boolean anyNumber, boolean canCancel,
                                       boolean forUpgrade, boolean forTransform, boolean forPurge, Predicate<AbstractCard>... predicate) {
        this.msg = msg;
        this.cm = cm;
        this.anyNumber = anyNumber;
        this.canCancel = canCancel;
        this.forUpgrade = forUpgrade;
        this.forTransform = forTransform;
        this.forPurge = forPurge;
        this.predicate = predicate[0];
        if (predicate.length > 1) {
            for (int i = 1; i < predicate.length; i++) {
                this.predicate = this.predicate.or(predicate[i]);
            }
        }
        tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        displayInOrder = false;
        stopGlowing = true;
        gridOpened = false;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        BaseMod.subscribe(this);
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(String msg, GridCardManipulator cm, boolean shouldMatchAll, boolean anyNumber, boolean canCancel,
                                       Predicate<AbstractCard>... predicate) {
        this(msg, cm, anyNumber, canCancel, false, false, false, predicate);
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(GridCardManipulator cm, Predicate<AbstractCard>... predicate) {
        this(null, cm, false, false, false, false, false, predicate);
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(Predicate<AbstractCard>... predicate) {
        this(null, null, false, false, false, false, false, predicate);
    }

    public SimpleGridCardSelectBuilder setCardGroup(CardGroup... cardGroups) {
        this.cardGroups = cardGroups;
        return this;
    }

    public SimpleGridCardSelectBuilder setDisplayInOrder(boolean displayInOrder) {
        this.displayInOrder = displayInOrder;
        return this;
    }

    public SimpleGridCardSelectBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public SimpleGridCardSelectBuilder setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public SimpleGridCardSelectBuilder setManipulator(GridCardManipulator cm) {
        this.cm = cm;
        return this;
    }

    public SimpleGridCardSelectBuilder setAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }

    public SimpleGridCardSelectBuilder setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }

    @Deprecated
    public SimpleGridCardSelectBuilder setShouldMatchAll(boolean shouldMatchAll) {
        this.shouldMatchAll = shouldMatchAll;
        return this;
    }

    public SimpleGridCardSelectBuilder setForUpgrade(boolean forUpgrade) {
        this.forUpgrade = forUpgrade;
        return this;
    }

    public SimpleGridCardSelectBuilder setForTransform(boolean forTransform) {
        this.forTransform = forTransform;
        return this;
    }

    public SimpleGridCardSelectBuilder setForPurge(boolean forPurge) {
        this.forPurge = forPurge;
        return this;
    }

    public SimpleGridCardSelectBuilder stopGlowing(boolean stopGlowing) {
        this.stopGlowing = stopGlowing;
        return this;
    }

    @Override
    public void update() {
        if (!gridOpened) {
            gridOpened = true;
            if (cardGroups == null) {
                isDone = true;
                BaseMod.unsubscribe(this);
                return;
            }
            tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (int i = 0; i < cardGroups.length; i++) {
                if (cardGroups[i].isEmpty()) continue;
                for (AbstractCard card : cardGroups[i].group) {
                    if (predicate.test(card)) {
                        if (stopGlowing && card.isGlowing)
                            card.stopGlowing();
                        if (displayInOrder)
                            tmpGroup.addToBottom(card);
                        else
                            tmpGroup.addToRandomSpot(card);
                        cardGroupMap.put(card, cardGroups[i]);
                    }
                }
            }
            if (tmpGroup.isEmpty()) {
                isDone = true;
                BaseMod.unsubscribe(this);
                return;
            }
            if (anyNumber) {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, true, msg);
            } else {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, msg, forUpgrade, forTransform, canCancel, forPurge);
            }
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() >= amount 
                || AbstractDungeon.gridSelectScreen.confirmButton.hb.clickStarted) {
            collect();
        }
        if (isDone) {
            BaseMod.unsubscribe(this);
        }
    }

    private void collect() {
        int index = 0;
        for (AbstractCard card : tmpGroup.group) {
            CardGroup group = cardGroupMap.get(card);
            if (AbstractDungeon.gridSelectScreen.selectedCards.contains(card) && group != null && !group.isEmpty()) {
                if (cm.manipulate(card, index, group)) {
                    // group.removeCard(card);
                }
                index++;
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        AbstractDungeon.player.hand.refreshHandLayout();
        isDone = true;
        tmpGroup.clear();
    }

    @Override
    public void receivePostDungeonUpdate() {
        if ((AbstractDungeon.actionManager.actions.contains(this) || AbstractDungeon.actionManager.currentAction == this)
                && anyNumber && AbstractDungeon.gridSelectScreen.confirmButton.hb.clickStarted) {
            BaseMod.unsubscribeLater(this);
            collect();
        }
    }
}
