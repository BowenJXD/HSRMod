package hsrmod.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.rare.IronCavalryAgainstTheScourge;
import hsrmod.relics.rare.PrisonerInDeepConfinement;
import hsrmod.relics.rare.TheAshblazingGrandDuke;
import hsrmod.relics.starter.WaxOfDestruction;
import hsrmod.relics.starter.WaxOfElation;
import hsrmod.relics.starter.WaxOfNihility;
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

    public void update(AbstractRoom room) {
        if (AbstractDungeon.combatRewardScreen.rewards.stream().anyMatch(r -> r.type == RewardItem.RewardType.CARD)
                && room != currRoom) {
            List<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
            for (RewardItem reward : rewards) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    reward.cards = getRewardCards(reward);
                    currRoom = room;
                    if (reward.cards.contains(null)) {
                        logger.info("CardRewardPoolEditor: Null card detected in reward pool.");
                    }
                    break;
                }
            }
            if (AbstractDungeon.actNum == 1 && AbstractDungeon.bossCount > 0) {
                String relicName = "";
                if (AbstractDungeon.player.hasRelic(HSRMod.makePath(WaxOfElation.ID))) {
                    relicName = TheAshblazingGrandDuke.ID;
                }
                if (AbstractDungeon.player.hasRelic(HSRMod.makePath(WaxOfDestruction.ID))) {
                    relicName = IronCavalryAgainstTheScourge.ID;
                }
                if (AbstractDungeon.player.hasRelic(HSRMod.makePath(WaxOfNihility.ID))) {
                    relicName = PrisonerInDeepConfinement.ID;
                }

                if (!relicName.isEmpty())
                    rewards.add(new RewardItem(RelicLibrary.getRelic(HSRMod.makePath(relicName)).makeCopy()));
            }
        }
    }

    public static ArrayList<AbstractCard> getRewardCards(RewardItem reward) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < reward.cards.size(); i++) {
            AbstractCard card = reward.cards.get(i);
            
            if (card.color == AbstractCard.CardColor.COLORLESS) {
                cards.add(card);
                continue;
            }
            
            AbstractCard newCard = getCard(card.rarity);
            
            if (newCard == null) {
                newCard = card;
            } else if (card.upgraded) {
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
        int randomValue = AbstractDungeon.cardRandomRng.random(totalWeight);

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
