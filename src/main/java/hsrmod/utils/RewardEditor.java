package hsrmod.utils;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import hsrmod.characters.StellaCharacter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.relics.common.RubertEmpireMechanicalCogwheel;
import hsrmod.relics.common.RubertEmpireMechanicalLever;
import hsrmod.relics.common.RubertEmpireMechanicalPiston;
import hsrmod.relics.starter.TrailblazeTimer;
import hsrmod.relics.starter.WaxRelic;
import hsrmod.subscribers.SubscriptionManager;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static basemod.BaseMod.logger;

/**
 * Singleton class for editing the card reward pool.
 */
public class RewardEditor implements StartActSubscriber, CustomSavable<String[]>, PostUpdateSubscriber {
    private static RewardEditor instance;

    AbstractRoom currRoom;

    public AbstractCard.CardTags tag;

    public String relicId = "";

    public List<AbstractCard.CardTags> bannedTags;

    private List<Consumer<List<RewardItem>>> extraRewards;
    @Deprecated
    private List<RewardItem> savedCardRewards;

    private RewardEditor() {
        bannedTags = new ArrayList<>();
        extraRewards = new ArrayList<>();
        savedCardRewards = new ArrayList<>();
        BaseMod.subscribe(this);
    }

    public static RewardEditor getInstance() {
        if (instance == null) {
            instance = new RewardEditor();
        }
        return instance;
    }

    public void update(AbstractRoom room, AbstractCard.CardTags tag) {
        if (room.rewardTime && room != currRoom) {
            currRoom = room;
            this.tag = tag;

            List<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
            
            for (Consumer<List<RewardItem>> extraReward : extraRewards) {
                extraReward.accept(rewards);
            }
            
            if (tag == null) return;

            for (RewardItem reward : rewards) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    setRewardByPath(reward);
                }
            }

            if (Objects.equals(relicId, "") && AbstractDungeon.player instanceof StellaCharacter) {
                try {
                    checkBossRelic(tag);
                } catch (Exception e) {
                    logger.error("Error checking boss relic: {}", e.getMessage());
                }
            }
        }

        if (AbstractDungeon.player != null
                && AbstractDungeon.player.relics != null
                && relicId != null
                && !Objects.equals(relicId, "")
                && AbstractDungeon.player.relics.stream().noneMatch(r -> r.relicId.equals(relicId))
                && currRoom instanceof MonsterRoomBoss
                && AbstractDungeon.combatRewardScreen.rewards.stream().noneMatch(r -> r.relic != null && r.relic.relicId.equals(relicId))) {
            RelicEventHelper.removeRelicFromPool(relicId);
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(RelicLibrary.getRelic(relicId).makeCopy()));
        }
    }

    /**
     * Check for extra relic reward to give in boss room.
     *
     * @param tag the path tag
     */
    private void checkBossRelic(AbstractCard.CardTags tag) {
        if (AbstractDungeon.getMonsters() != null && currRoom instanceof MonsterRoomBoss) {
            if (AbstractDungeon.actNum == 1)
                relicId = RelicEventHelper.getRelicByPath(tag);
            else if (AbstractDungeon.actNum == 2)
                relicId = RelicEventHelper.getHSRRelic(AbstractRelic.RelicTier.RARE);
            else if (AbstractDungeon.actNum == 3) {
                List<String> empireRelics = new ArrayList<>();
                if (!ModHelper.hasRelic(RubertEmpireMechanicalLever.ID) || AbstractDungeon.player.getRelic(RubertEmpireMechanicalLever.ID).usedUp)
                    empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalLever.ID));
                if (!ModHelper.hasRelic(RubertEmpireMechanicalPiston.ID) || AbstractDungeon.player.getRelic(RubertEmpireMechanicalPiston.ID).usedUp)
                    empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalPiston.ID));
                if (!ModHelper.hasRelic(RubertEmpireMechanicalCogwheel.ID) || AbstractDungeon.player.getRelic(RubertEmpireMechanicalCogwheel.ID).usedUp)
                    empireRelics.add(HSRMod.makePath(RubertEmpireMechanicalCogwheel.ID));
                if (empireRelics.size() == 1 && AbstractDungeon.miscRng.randomBoolean()) {
                    relicId = empireRelics.get(0);
                }
            }
        }
    }
    
    public void setRewardByPath(RewardItem reward) {
        setRewardByPath(tag, reward, false);
    }
    
    public void setRewardByPath(RewardItem reward, boolean ignoreChance) {
        setRewardByPath(tag, reward, ignoreChance);
    }
    
    public void setRewardByPath(AbstractCard.CardTags tag, RewardItem reward) {
        setRewardByPath(tag, reward, false);
    }

    /**
     * Set the reward cards to be within the specified path if chance is met.
     *
     * @param reward the reward item to modify
     */
    public void setRewardByPath(AbstractCard.CardTags tag, RewardItem reward, boolean ignoreChance) {
        for (int i = 0, j = 0; i < reward.cards.size() && j < 1000; i++, j++) {
            AbstractCard card = reward.cards.get(i);

            // skip colorless cards
            if (card.color == AbstractCard.CardColor.COLORLESS) continue;
            // skip if card path is not banned and chance is not met
            if (checkPath(card) && !checkChance(card.rarity) && !ignoreChance) continue;
            // skip if card path is already the selected path
            if (card.tags.contains(tag)) continue;

            // change card to a new card of the selected path
            AbstractCard newCard = getCardByPath(tag, card.rarity, reward.cards);

            // skip if new card is null or repeated
            if (newCard == null) continue;
            if (reward.cards.stream().anyMatch(c -> Objects.equals(c.cardID, newCard.cardID))) {
                i--;
                continue;
            }

            // upgrade new card if the original card is upgraded
            if (card.upgraded) newCard.upgrade();

            reward.cards.set(i, newCard);
        }
    }

    /**
     * Set the reward cards to be within the specified rarities, if not, set to the least common rarity.
     *
     * @param reward   the reward item to modify
     * @param rarities the rarities to set the cards to
     */
    public static void setRewardRarity(RewardItem reward, AbstractCard.CardRarity... rarities) {
        if (rarities.length == 0) return;
        List<AbstractCard.CardRarity> rarityList = Arrays.asList(rarities);
        for (int i = 0, j = 0; i < reward.cards.size() && j < 1000; i++, j++) {
            if (!rarityList.contains(reward.cards.get(i).rarity)) {
                AbstractCard newCard = AbstractDungeon.getCard(rarities[0]);
                if (reward.cards.stream().anyMatch(c -> Objects.equals(c.cardID, newCard.cardID))) i--;
                else reward.cards.set(i, newCard);
            }
        }
    }
    
    public AbstractCard getCardByPath(AbstractCard.CardRarity rarity, ArrayList<AbstractCard> currentRewardCards) {
        return getCardByPath(tag, rarity, currentRewardCards);
    }

    /**
     * Get a card of the specified rarity and path.
     *
     * @param rarity             the rarity of the card
     * @param currentRewardCards the current reward cards
     * @return the card to change
     */
    public AbstractCard getCardByPath(AbstractCard.CardTags tag, AbstractCard.CardRarity rarity, ArrayList<AbstractCard> currentRewardCards) {
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

        List<AbstractCard> cards = group.group.stream()
                .filter(c -> c.hasTag(tag) || (tag == CustomEnums.TRAILBLAZE && checkPath(c)))
                .filter(c -> AbstractDungeon.player.masterDeck.group.stream().noneMatch(card -> c.uuid == card.uuid))
                .filter(c -> c instanceof SpawnModificationCard && ((SpawnModificationCard) c).canSpawn(currentRewardCards == null ? new ArrayList<>() : currentRewardCards))
                .collect(Collectors.toList());

        if (cards.isEmpty()) return null;
        AbstractCard result = cards.get(AbstractDungeon.cardRng.random(cards.size() - 1));
        result.unfadeOut();
        return result.makeCopy();
    }

    /**
     * Check the chance of changing a card of the specified rarity.
     *
     * @param rarity the rarity of the card
     * @return true if the chance is met
     */
    public boolean checkChance(AbstractCard.CardRarity rarity) {
        float chance = 0;
        int[] commonChance      = new int[]{30, 15, 10, 5};
        int[] uncommonChance    = new int[]{40, 20, 10, 5};
        int[] rareChance        = new int[]{50, 30, 20, 10};
        int actIndex = Math.min(AbstractDungeon.actNum - 1, 3);
        if (actIndex < 0) actIndex = 0;
        switch (rarity) {
            case COMMON:
                chance = commonChance[actIndex];
                break;
            case UNCOMMON:
                chance = uncommonChance[actIndex];
                break;
            case RARE:
                chance = rareChance[actIndex];
                break;
        }
        chance = SubscriptionManager.getInstance().triggerNumChanger(SubscriptionManager.NumChangerType.WAX_WEIGHT, chance);
        return AbstractDungeon.cardRandomRng.random(99) < chance;
    }

    /**
     * Check if the card's path is banned.
     *
     * @param card the card to check
     * @return true if the card's path is not banned
     */
    public boolean checkPath(AbstractCard card) {
        if (card.tags == null || card.tags.isEmpty() || bannedTags == null) return true;
        return card.tags.stream().noneMatch(t -> bannedTags.contains(t));
    }

    /**
     * Check if the path is banned.
     *
     * @param tag the path tag
     */
    public boolean checkPath(AbstractCard.CardTags tag) {
        if (bannedTags == null) return true;
        else return !bannedTags.contains(tag);
    }

    @Override
    public String[] onSave() {
        String[] result = new String[2];
        if (bannedTags == null) result[0] = "";
        else result[0] = bannedTags.toString();
        // result[1] = saveCardRewards();
        return result;
    }

    @Deprecated
    String saveCardRewards() {
        StringBuilder result = new StringBuilder();
        if (AbstractDungeon.currMapNode == null) return "";
        for (RewardItem reward : AbstractDungeon.currMapNode.room.rewards) {
            if (reward.type == RewardItem.RewardType.CARD) {
                result.append("[");
                for (int i = 0; i < reward.cards.size(); i++) {
                    result.append(reward.cards.get(i).cardID);
                    if (reward.cards.get(i).upgraded) result.append("+");
                    if (i < reward.cards.size() - 1) result.append(", ");
                }
                result.append("];");
            }
        }
        return result.toString();
    }

    @Override
    public void onLoad(String[] data) {
        if (data == null) return;

        String cardTags = data[0];
        if (bannedTags == null) {
            bannedTags = new ArrayList<>();
        } else {
            bannedTags = GeneralUtil.unpackSaveData(cardTags, AbstractCard.CardTags::valueOf);
        }

        // String cardRewards = data[1];
        // loadCardRewards(cardRewards);
    }

    @Deprecated
    void loadCardRewards(String data) {
        if (data == null || data.isEmpty()) return;
        String[] rewards = data.split(";");
        savedCardRewards.clear();
        for (String reward : rewards) {
            List<String> cards = GeneralUtil.unpackSaveData(reward, String::valueOf);
            try {
                ArrayList<AbstractCard> cardList = cards.stream().map(string -> {
                    AbstractCard result;
                    if (string.endsWith("+")) {
                        string = string.substring(0, string.length() - 1);
                        result = CardLibrary.getCard(string);
                        result.upgrade();
                    } else {
                        result = CardLibrary.getCard(string);
                    }
                    return result;
                }).collect(Collectors.toCollection(ArrayList::new));
                ModHelper.addEffectAbstract(() -> {
                    RewardItem rewardItem = new RewardItem(StellaCharacter.PlayerColorEnum.HSR_PINK);
                    rewardItem.cards = cardList;
                    savedCardRewards.add(rewardItem);
                });
            } catch (Exception e) {
                logger.error("Error loading card rewards: {}", e.getMessage());
            }
        }
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.currMapNode == null) return;
        this.update(AbstractDungeon.getCurrRoom(), tag);
    }

    @Override
    public void receiveStartAct() {
        if (AbstractDungeon.actNum <= 1) {
            relicId = "";
            bannedTags = null;
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof WaxRelic) {
                    ((WaxRelic) relic).updateDescription(relic.getUpdatedDescription());
                }
                if (relic instanceof TrailblazeTimer) {
                    ((TrailblazeTimer) relic).updateDescription(relic.getUpdatedDescription());
                }
            }
            if (AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.player.gold < HSRModConfig.getActiveTPCount() * 200) {
                AbstractDungeon.player.gainGold(HSRModConfig.getActiveTPCount() * 200);
            }
        }
    }

    public static void addExtraRewardToTop(Consumer<List<RewardItem>> extraReward) {
        getInstance().extraRewards.add(0, extraReward);
    }
    
    public static void addExtraCardRewardToTop() {
        getInstance().extraRewards.add(0, rewards -> rewards.add(new RewardItem()));
    }

    public static void addExtraRewardToBot(Consumer<List<RewardItem>> extraReward) {
        getInstance().extraRewards.add(extraReward);
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class EnterRoomPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            ModHelper.addEffectAbstract(() -> {
                if (instance != null
                        && (AbstractDungeon.currMapNode == null || !AbstractDungeon.getCurrRoom().rewardTime)) {
                    instance.relicId = "";
                    instance.extraRewards.clear();
                    // instance.savedCardRewards.clear();
                }
            });
        }
    }
}
