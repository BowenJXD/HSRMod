package hsrmod.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.rare.IronCavalryAgainstTheScourge;
import hsrmod.relics.rare.PrisonerInDeepConfinement;
import hsrmod.relics.rare.TheAshblazingGrandDuke;

import java.util.*;

import static basemod.BaseMod.logger;

/**
 * Singleton class for editing the card reward pool.
 */
public class CardRewardPoolEditor {
    private static CardRewardPoolEditor instance;

    AbstractRoom currRoom;
    
    AbstractCard.CardTags tag;
    
    private CardRewardPoolEditor() {
    }

    public static CardRewardPoolEditor getInstance() {
        if (instance == null) {
            instance = new CardRewardPoolEditor();
        }
        return instance;
    }

    public void update(AbstractRoom room, AbstractCard.CardTags tag) {
        if (AbstractDungeon.combatRewardScreen.rewards.stream().anyMatch(r -> r.type == RewardItem.RewardType.CARD)
                && room != currRoom) {
            this.tag = tag;
            List<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
            for (RewardItem reward : rewards) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    setRewardCards(reward);
                    currRoom = room;
                    if (reward.cards.contains(null)) {
                        logger.info("CardRewardPoolEditor: Null card detected in reward pool.");
                    }
                    break;
                }
            }
            if (AbstractDungeon.actNum == 1 && AbstractDungeon.bossCount > 0) {
                String relicName = "";
                if (tag == CustomEnums.ELATION) {
                    relicName = TheAshblazingGrandDuke.ID;
                }
                if (tag == CustomEnums.DESTRUCTION) {
                    relicName = IronCavalryAgainstTheScourge.ID;
                }
                if (tag == CustomEnums.NIHILITY) {
                    relicName = PrisonerInDeepConfinement.ID;
                }

                if (!relicName.isEmpty())
                    rewards.add(new RewardItem(RelicLibrary.getRelic(HSRMod.makePath(relicName)).makeCopy()));
            }
        }
    }

    public void setRewardCards(RewardItem reward) {
        for (int i = 0; i < reward.cards.size(); i++) {
            AbstractCard card = reward.cards.get(i);
            
            if (card.color == AbstractCard.CardColor.COLORLESS
                    || !checkChance(card.rarity)) {
                continue;
            }
            
            AbstractCard newCard = getCard(card.rarity);
            
            if (reward.cards.stream().anyMatch(c -> Objects.equals(c.cardID, newCard.cardID)) || newCard == null) {
                continue;
            }
            
            if (card.upgraded) {
                newCard.upgrade();
            }
            
            reward.cards.set(i, newCard);
        }
    }

    public AbstractCard getCard(AbstractCard.CardRarity rarity) {
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

        ArrayList<AbstractCard> cards = group.group.stream().filter(c -> c.hasTag(tag)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
    }
    
    public boolean checkChance(AbstractCard.CardRarity rarity){
        float chance = 0;
        switch (rarity) {
            case COMMON:
                chance = 40 - AbstractDungeon.actNum * 10; 
                break;
            case UNCOMMON:
                chance = 50 - AbstractDungeon.actNum * 10; 
                break;
            case RARE:
                chance = 60 - AbstractDungeon.actNum * 10; 
                break;
        }
        return AbstractDungeon.cardRandomRng.random(99) < chance;
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
