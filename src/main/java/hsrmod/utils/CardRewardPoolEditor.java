package hsrmod.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.compiler.ast.Variable;

import java.util.*;
import java.util.function.Predicate;

import static basemod.BaseMod.logger;

/**
 * Singleton class for editing the card reward pool.
 */
public class CardRewardPoolEditor {
    private static CardRewardPoolEditor instance;
    
    AbstractRoom currRoom;
    
    Map<Integer, List<Predicate<AbstractCard>>> weightMap;

    private CardRewardPoolEditor() {
        weightMap = new HashMap<>();
    }

    public static CardRewardPoolEditor getInstance() {
        if (instance == null) {
            instance = new CardRewardPoolEditor();
        }
        return instance;
    }
    
    public void RegisterWeight(int weight, Predicate<AbstractCard> predicate) {
        if (!weightMap.containsKey(weight)) {
            weightMap.put(weight, new ArrayList<>());
        }
        weightMap.get(weight).add(predicate);
    }
    
    public void UnregisterWeight(int weight, Predicate<AbstractCard> predicate) {
        if (weightMap.containsKey(weight)) {
            weightMap.get(weight).remove(predicate);
        }
    }

    public void update(AbstractRoom room){
        if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()
                && room != currRoom) {
            List<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
            for (RewardItem reward : rewards) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    reward.cards = getRewardCards(reward);
                    currRoom = room;
                    break;
                }
            }
        }
    }

    public static ArrayList<AbstractCard> getRewardCards(RewardItem reward) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < reward.cards.size(); i++) {
            AbstractCard card = reward.cards.get(i);
            AbstractCard newCard = getCard(card.rarity);
            if (newCard == null) {
                newCard = card;
            }
            else if (card.upgraded) {
                newCard.upgrade();
            }
            cards.add(newCard);
        }
        return cards;
    }

    public static AbstractCard getCard(AbstractCard.CardRarity rarity) {
        CardGroup group = null;
        switch (rarity) {
            case COMMON:
                group = AbstractDungeon.commonCardPool;
                break;
            case UNCOMMON:
                group = AbstractDungeon.uncommonCardPool;
                break;
            case RARE:
                group = AbstractDungeon.rareCardPool;
                break;
            case CURSE:
                group = AbstractDungeon.curseCardPool;
                break;
            default:
                logger.info("No rarity on getCard in Abstract Dungeon");
                return null;
        }

        ArrayList<Integer> weights = new ArrayList<>();
        for (AbstractCard card : group.group) {
            int weight = 1;
            for (Integer kvp : instance.weightMap.keySet()) {
                if (instance.weightMap.get(kvp).stream().anyMatch(p -> p.test(card))) {
                    weight += kvp;
                }
            }
            weights.add(weight);
        }
        
        return getWeightedRandomElement(group.group, weights);
    }

    public static <T> T getWeightedRandomElement(List<T> items, List<Integer> weights) {
        if (items == null || weights == null || items.size() != weights.size() || items.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: items and weights must be non-null and have the same size.");
        }

        // Calculate the total sum of the weights
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        // Generate a random number between 0 and totalWeight
        Random random = new Random();
        int randomValue = random.nextInt(totalWeight);

        // Iterate over the weights to find the corresponding item
        for (int i = 0; i < items.size(); i++) {
            randomValue -= weights.get(i);
            if (randomValue < 0) {
                return items.get(i);
            }
        }

        // Fallback (should never happen if input is valid)
        return null;
    }
}
