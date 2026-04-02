package hsrmod.utils;

import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * util for {@link com.megacrit.cardcrawl.actions.GameActionManager}
 */
public class GAMManager implements PostDungeonUpdateSubscriber, PostBattleSubscriber, OnStartBattleSubscriber {
    private static GAMManager instance = null;

    public HashMap<String, Predicate<AbstractGameAction>> parallelActions = new HashMap<>();
    public HashMap<String, Predicate<AbstractGameAction>> actionsCache = new HashMap<>();
    public AbstractGameAction currentAction;

    public AbstractCard currentCard;

    private GAMManager() {
        update();
    }

    public static GAMManager getInstance() {
        if (instance == null) {
            instance = new GAMManager();
        }
        return instance;
    }

    public static void addParallelAction(String id, Predicate<AbstractGameAction> action) {
        getInstance().parallelActions.put(id, action);
        getInstance().actionsCache.put(id, action);
    }

    public static void removeParallelAction(String id) {
        getInstance().parallelActions.remove(id);
    }

    @Override
    public void receivePostDungeonUpdate() {
        update();
    }

    void update() {
        if (AbstractDungeon.actionManager == null) return;
        if (currentAction != AbstractDungeon.actionManager.currentAction) {
            currentAction = AbstractDungeon.actionManager.currentAction;
            if (currentAction != null) {
                System.out.printf("action: %-50s | source: %-30s | target: %-30s | amount: %-4d%n",
                        currentAction.getClass().getSimpleName().isEmpty() ? currentAction : currentAction.getClass().getSimpleName() + '@' + currentAction.hashCode(),
                        currentAction.source != null ? currentAction.source.getClass().getSimpleName() + '@' + currentAction.source.hashCode() : "null",
                        currentAction.target != null ? currentAction.target.getClass().getSimpleName() + '@' + currentAction.target.hashCode() : "null",
                        currentAction.amount);
                Iterator<Map.Entry<String, Predicate<AbstractGameAction>>> iterator = parallelActions.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Predicate<AbstractGameAction>> entry = iterator.next();
                    if (entry.getValue().test(currentAction)) {
                        System.out.printf("Parallel action %s removed by %s%n", entry.getKey(), currentAction);
                        iterator.remove();
                    }
                }
            }
        }
        List<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (!cards.isEmpty() && cards.get(cards.size() - 1) != currentCard) {
            currentCard = cards.get(cards.size() - 1);
            int cardIndex = cards.size();
            System.out.printf("================== turn %-2d card %-2d: %-20s, D: %-3d, B: %-3d, M: %-3d%n", GameActionManager.turn, cardIndex,
                    currentCard, currentCard.damage, currentCard.block, currentCard.magicNumber);
        }
    }

    public static void stopCurrentAction() {
        AbstractDungeon.actionManager.previousAction = AbstractDungeon.actionManager.currentAction;
        AbstractDungeon.actionManager.currentAction = null;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        parallelActions.clear();
        currentCard = null;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        parallelActions.clear();
        currentCard = null;
    }
}

