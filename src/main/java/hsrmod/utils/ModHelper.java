package hsrmod.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ModHelper {
    public static void addToBotAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public static void addToTopAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public interface Lambda extends Runnable {}
    
    public static List<AbstractGameAction> mostBotList = new ArrayList<>();
    
    public static void addToMostBot(Lambda func){
        AbstractGameAction action = new AbstractGameAction() {
            @Override
            public void update() {
                isDone = !isDone;
                if (!isDone) return;
                if (new HashSet<>(mostBotList).containsAll(AbstractDungeon.actionManager.actions)) {
                    func.run();
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(this);
                }
            }
        };
        mostBotList.add(action);
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static <T extends Enum<T>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int randomIndex = new Random().nextInt(values.length);
        return values[randomIndex];
    }
    
    public static int getPowerCount(String powerID) {
        return AbstractDungeon.player.hasPower(powerID) ? AbstractDungeon.player.getPower(powerID).amount : 0;
    }
    
    public static int getPowerCount(AbstractCreature creature, String powerID) {
        return creature.hasPower(powerID) ? creature.getPower(powerID).amount : 0;
    }
    
    public static List<FindResult> findCardsInGroup(Predicate<AbstractCard> predicate, CardGroup group) {
        List<FindResult> result = new ArrayList<>();
        for (AbstractCard card : group.group) {
            if (predicate.test(card)) {
                FindResult findResult = new FindResult();
                findResult.card = card;
                findResult.group = group;
                result.add(findResult);
            }
        }
        return result;
    }
    
    public static List<FindResult> findCards(Predicate<AbstractCard> predicate, boolean hand, boolean discard, boolean draw, boolean exhaust, boolean limbo) {
        List<FindResult> result = new ArrayList<>();
        if (hand) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.hand));
        if (discard) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.discardPile));
        if (draw) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.drawPile));
        if (exhaust) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.exhaustPile));
        if (limbo) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.limbo));
        return result;
    }
    
    public static List<FindResult> findCards(Predicate<AbstractCard> predicate) {
        return findCards(predicate, true, true, true, true, true);
    }
    
    public static class FindResult {
        public AbstractCard card;
        public CardGroup group;
    }
}
