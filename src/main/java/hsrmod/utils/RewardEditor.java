package hsrmod.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;
import hsrmod.relics.boss.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static basemod.BaseMod.logger;

/**
 * Singleton class for editing the card reward pool.
 */
public class RewardEditor {
    private static RewardEditor instance;

    AbstractRoom currRoom;

    AbstractCard.CardTags tag;

    private RewardEditor() {
    }

    public static RewardEditor getInstance() {
        if (instance == null) {
            instance = new RewardEditor();
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
            
            if (AbstractDungeon.actNum == 1
                    && AbstractDungeon.getMonsters() != null
                    && AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.type == AbstractMonster.EnemyType.BOSS)) {
                
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
                if (tag == CustomEnums.PRESERVATION) {
                    relicName = KnightOfPurityPalace.ID;
                }
                if (tag == CustomEnums.THE_HUNT) {
                    relicName = MusketeerOfWildWheat.ID;
                }
                if (tag == CustomEnums.PROPAGATION) {
                    relicName = PasserbyOfWanderingCloud.ID;
                }

                if (!relicName.isEmpty())
                    rewards.add(new RewardItem(RelicLibrary.getRelic(HSRMod.makePath(relicName)).makeCopy()));
            } else if (AbstractDungeon.actNum == 2 
                    && AbstractDungeon.getMonsters() != null
                    && AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.type == AbstractMonster.EnemyType.BOSS)) {
                String relicName = getRelic(AbstractRelic.RelicTier.RARE);

                if (!relicName.isEmpty())
                    rewards.add(new RewardItem(RelicLibrary.getRelic(relicName).makeCopy()));
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

            if (newCard == null || reward.cards.stream().anyMatch(c -> Objects.equals(c.cardID, newCard.cardID))) {
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

        ArrayList<AbstractCard> cards = group.group.stream()
                .filter(c -> c.hasTag(tag))
                .filter(c -> AbstractDungeon.player.masterDeck.group.stream().noneMatch(card -> c.uuid == card.uuid))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (cards.isEmpty()) return null;
        return cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
    }

    public String getRelic(AbstractRelic.RelicTier tier) {
        ArrayList<String> pool = new ArrayList<>();
        switch (tier) {
            case COMMON:
                pool = AbstractDungeon.commonRelicPool;
                break;
            case UNCOMMON:
                pool = AbstractDungeon.uncommonRelicPool;
                break;
            case RARE:
                pool = AbstractDungeon.rareRelicPool;
                break;
            case SHOP:
                pool = AbstractDungeon.shopRelicPool;
                break;
            case BOSS:
                pool = AbstractDungeon.bossRelicPool;
                break;
        }

        if (pool.isEmpty()) {
            return "";
        }
        List<String> relics = pool.stream().filter(r -> RelicLibrary.getRelic(r) instanceof BaseRelic).collect(Collectors.toList());
        String relic = relics.get(AbstractDungeon.cardRandomRng.random(relics.size() - 1));
        pool.remove(relic);
        return relic;
    }

    public boolean checkChance(AbstractCard.CardRarity rarity) {
        float chance = 0;
        switch (rarity) {
            case COMMON:
                chance = 30 - AbstractDungeon.actNum * 10;
                break;
            case UNCOMMON:
                chance = 40 - AbstractDungeon.actNum * 10;
                break;
            case RARE:
                chance = 50 - AbstractDungeon.actNum * 10;
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
