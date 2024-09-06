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
                    reward.cards = getRewardCards();
                    currRoom = room;
                    break;
                }
            }
        }
    }

    public static ArrayList<AbstractCard> getRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList();
        int numCards = 3;

        AbstractRelic r;
        for(Iterator var2 = AbstractDungeon.player.relics.iterator(); var2.hasNext(); numCards = r.changeNumberOfCardsInReward(numCards)) {
            r = (AbstractRelic)var2.next();
        }

        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }

        AbstractCard card;
        for(int i = 0; i < numCards; ++i) {
            AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
            card = null;
            switch (rarity) {
                case COMMON:
                    AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
                    if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset) {
                        AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
                    }
                case UNCOMMON:
                    break;
                case RARE:
                    AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzStartOffset;
                    break;
                default:
                    logger.info("WTF?");
            }

            boolean containsDupe = true;

            while(true) {
                while(containsDupe) {
                    containsDupe = false;
                    if (AbstractDungeon.player.hasRelic("PrismaticShard")) {
                        card = CardLibrary.getAnyColorCard(rarity);
                    } else {
                        card = getCard(rarity);
                    }

                    Iterator var6 = retVal.iterator();

                    while(var6.hasNext()) {
                        AbstractCard c = (AbstractCard)var6.next();
                        if (c.cardID.equals(card.cardID)) {
                            containsDupe = true;
                            break;
                        }
                    }
                }

                if (card != null) {
                    retVal.add(card);
                }
                break;
            }
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList();
        Iterator var11 = retVal.iterator();

        while(var11.hasNext()) {
            card = (AbstractCard)var11.next();
            retVal2.add(card.makeCopy());
        }

        var11 = retVal2.iterator();

        while(true) {
            while(var11.hasNext()) {
                card = (AbstractCard)var11.next();
                if (card.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(0.1f) && card.canUpgrade()) {
                    card.upgrade();
                } else {
                    Iterator var12 = AbstractDungeon.player.relics.iterator();

                    while(var12.hasNext()) {
                        AbstractRelic relic = (AbstractRelic)var12.next();
                        relic.onPreviewObtainCard(card);
                    }
                }
            }

            return retVal2;
        }
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
