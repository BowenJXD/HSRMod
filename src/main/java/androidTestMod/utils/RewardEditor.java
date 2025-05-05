package androidTestMod.utils;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomSavable;
import com.megacrit.cardcrawl.android.mods.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.android.mods.interfaces.StartActSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import androidTestMod.cards.BaseCard;
import androidTestMod.characters.StellaCharacter;
import androidTestMod.effects.TopWarningEffect;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.modcore.HSRModConfig;
import androidTestMod.relics.common.RubertEmpireMechanicalCogwheel;
import androidTestMod.relics.common.RubertEmpireMechanicalLever;
import androidTestMod.relics.common.RubertEmpireMechanicalPiston;
import androidTestMod.relics.starter.TrailblazeTimer;
import androidTestMod.relics.starter.WaxRelic;
import androidTestMod.subscribers.SubscriptionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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
                }
            }
        }

        if (AbstractDungeon.player != null
                && AbstractDungeon.player.relics != null
                && relicId != null
                && !Objects.equals(relicId, "")) {
            boolean result = true;
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic.relicId.equals(relicId)) {
                    result = false;
                    break;
                }
            }
            if (result
                    && currRoom instanceof MonsterRoomBoss) {
                boolean b = true;
                for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
                    if (r.relic != null && r.relic.relicId.equals(relicId)) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    RelicEventHelper.removeRelicFromPool(relicId);
                    AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(RelicLibrary.getRelic(relicId).makeCopy()));
                }
            }
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
                    empireRelics.add(AndroidTestMod.makePath(RubertEmpireMechanicalLever.ID));
                if (!ModHelper.hasRelic(RubertEmpireMechanicalPiston.ID) || AbstractDungeon.player.getRelic(RubertEmpireMechanicalPiston.ID).usedUp)
                    empireRelics.add(AndroidTestMod.makePath(RubertEmpireMechanicalPiston.ID));
                if (!ModHelper.hasRelic(RubertEmpireMechanicalCogwheel.ID) || AbstractDungeon.player.getRelic(RubertEmpireMechanicalCogwheel.ID).usedUp)
                    empireRelics.add(AndroidTestMod.makePath(RubertEmpireMechanicalCogwheel.ID));
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
            boolean b = false;
            for (AbstractCard c : reward.cards) {
                if (Objects.equals(c.cardID, newCard.cardID)) {
                    b = true;
                    break;
                }
            }
            if (b) {
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
                boolean b = false;
                for (AbstractCard c : reward.cards) {
                    if (Objects.equals(c.cardID, newCard.cardID)) {
                        b = true;
                        break;
                    }
                }
                if (b) i--;
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
                return null;
        }

        List<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : group.group) {
            if (c.hasTag(tag) || (tag == CustomEnums.TRAILBLAZE && RewardEditor.this.checkPath(c))) {
                boolean isInMasterDeck = false;
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (c.uuid == card.uuid) {
                        isInMasterDeck = true;
                        break;
                    }
                }
                if (!isInMasterDeck) {
                    if (c instanceof BaseCard && ((BaseCard) c).checkSpawnable()) {
                        cards.add(c);
                    }
                }
            }
        }

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
        int[] commonChance = new int[]{30, 15, 10, 5};
        int[] uncommonChance = new int[]{40, 20, 10, 5};
        int[] rareChance = new int[]{50, 30, 20, 10};
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
        for (AbstractCard.CardTags t : card.tags) {
            if (bannedTags.contains(t)) {
                return false;
            }
        }
        return true;
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
                ArrayList<AbstractCard> cardList = new ArrayList<>();
                for (String card : cards) {
                    AbstractCard apply = (new Function<String, AbstractCard>() {
                        @Override
                        public AbstractCard apply(String string) {
                            AbstractCard result;
                            if (string.endsWith("+")) {
                                string = string.substring(0, string.length() - 1);
                                result = CardLibrary.getCard(string);
                                result.upgrade();
                            } else {
                                result = CardLibrary.getCard(string);
                            }
                            return result;
                        }
                    }).apply(card);
                    cardList.add(apply);
                }
                ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                    @Override
                    public void run() {
                        RewardItem rewardItem = new RewardItem(StellaCharacter.PlayerColorEnum.HSR_PINK);
                        rewardItem.cards = cardList;
                        savedCardRewards.add(rewardItem);
                    }
                });
            } catch (Exception e) {
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
            if (AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.player.gold < HSRModConfig.getGoldInc()) {
                AbstractDungeon.player.gainGold(HSRModConfig.getGoldInc());
                if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
                    AbstractDungeon.topLevelEffectsQueue.add(new TopWarningEffect("⚠阈值协议生效⚠"));
                } else {
                    AbstractDungeon.topLevelEffectsQueue.add(new TopWarningEffect("⚠THRESHOLD PROTOCOLS ACTIVATED⚠"));
                }
            }
        }
    }

    public static void addExtraRewardToTop(Consumer<List<RewardItem>> extraReward) {
        getInstance().extraRewards.add(0, extraReward);
    }

    public static void addExtraCardRewardToTop() {
        getInstance().extraRewards.add(0, new Consumer<List<RewardItem>>() {
            @Override
            public void accept(List<RewardItem> rewards) {
                rewards.add(new RewardItem());
            }
        });
    }

    public static void addExtraRewardToBot(Consumer<List<RewardItem>> extraReward) {
        getInstance().extraRewards.add(extraReward);
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class EnterRoomPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    if (instance != null
                            && (AbstractDungeon.currMapNode == null || !AbstractDungeon.getCurrRoom().rewardTime)) {
                        instance.relicId = "";
                        instance.extraRewards.clear();
                        // instance.savedCardRewards.clear();
                    }
                }
            });
        }
    }
}
