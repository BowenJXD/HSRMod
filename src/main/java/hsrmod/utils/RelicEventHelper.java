package hsrmod.utils;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.boss.*;
import hsrmod.relics.special.*;
import hsrmod.relics.special.TheWindSoaringValorous;
import hsrmod.relics.uncommon.JellyfishOnTheStaircase;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicEventHelper {

    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(HSRMod.makePath("RelicEventHelper"));
    public static String SELECT_TEXT = uiStrings.TEXT[0];
    public static String UPGRADE_TEXT = uiStrings.TEXT[1];
    public static String PURGE_TEXT = uiStrings.TEXT[2];
    public static String OBTAIN_TEXT = uiStrings.TEXT[3];
    public static String TRANSFORM_TEXT = uiStrings.TEXT[4];
    public static String REWARD_TEXT = uiStrings.TEXT[5];

    public static void upgradeCards(int amount) {
        int count = 0;
        List<String> upgradedCards = new ArrayList<>();
        List<AbstractCard> list = AbstractDungeon.player.masterDeck.group;
        Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));

        for (AbstractCard c : list) {
            if (c.canUpgrade() && !upgradedCards.contains(c.cardID)) {
                upgradedCards.add(c.cardID);
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);

                ++count;
                if (count <= 20) {
                    float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));
                }

                if (count >= amount) {
                    break;
                }
            }
        }
    }

    public static void upgradeCards(AbstractCard... card) {
        for (AbstractCard c : card) {
            if (c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));
            }
        }
    }

    public static void addReward(Consumer<List<RewardItem>> reward) {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            RewardEditor.addExtraRewardToTop(rewards -> {
                rewards.clear();
                reward.accept(rewards);
            });
            AbstractDungeon.combatRewardScreen.open(REWARD_TEXT);
        } else if (!AbstractDungeon.getCurrRoom().rewardTime) {
            RewardEditor.addExtraRewardToTop(reward);
        } else {
            ModHelper.addEffectAbstract(() -> {
                reward.accept(AbstractDungeon.combatRewardScreen.rewards);
            });
        }
    }

    public static void gainCards(AbstractCard... cards) {
        if (cards.length == 0) {
            HSRMod.logger.error("RELIC_EVENT_HELPER: No cards to gain.");
            return;
        }
        else if (cards.length == 1)
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(cards[0], (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        else if (cards.length <= 5){
            float x = 0;
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 6.0F + x, (float) Settings.HEIGHT / 2.0F));
                x += (float) Settings.WIDTH / 6.0F;
            }
        } else {
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH * MathUtils.random(0.2f, 0.8f), Settings.HEIGHT * MathUtils.random(0.2f, 0.8f)));
            }
        }
    }
    
    public static void purgeCards(AbstractCard... cards) {
        if (cards.length == 0) {
            HSRMod.logger.error("RELIC_EVENT_HELPER: No cards to purge.");
            return;
        }
        else if (cards.length == 1)
            AbstractDungeon.effectList.add(new PurgeCardEffect(cards[0], (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        else if (cards.length <= 5){
            float x = 0;
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 6.0F + x, (float) Settings.HEIGHT / 2.0F));
                x += (float) Settings.WIDTH / 6.0F;
            }
        } else {
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(card, Settings.WIDTH * MathUtils.random(0.2f, 0.8f), Settings.HEIGHT * MathUtils.random(0.2f, 0.8f)));
            }
        }
        AbstractDungeon.player.masterDeck.group.removeAll(Arrays.asList(cards));
    }

    public static void gainRelicsAfterwards(int amount) {
        ModHelper.addEffectAbstract(() -> gainRelics(amount));
    }

    public static void gainRelics(int amount) {
        gainRelics(amount, r -> !RelicTagField.subtle.get(r));
    }

    public static void gainRelics(int amount, Predicate<AbstractRelic> predicate) {
        List<AbstractRelic> relics = getRelics(amount, predicate);
        for (AbstractRelic relic : relics) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
        }
    }

    public static List<AbstractRelic> getRelics(int amount, Predicate<AbstractRelic> predicate) {
        List<AbstractRelic> relics = new ArrayList<>();
        for (int i = 0, j = 0; i < amount && j < 99; ++i, ++j) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            if (!predicate.test(r) || r instanceof JellyfishOnTheStaircase) {
                --i;
                addRelicToPool(r);
            } else {
                relics.add(r);
            }
        }
        return relics;
    }
    
    public static void rewardRelics(int amount) {
        rewardRelics(amount, r -> true);
    }
    
    public static void rewardRelics(int amount, Predicate<AbstractRelic> predicate) {
        addReward(rewards -> {
            rewards.clear();
            List<AbstractRelic> relics = getRelics(amount, predicate);
            for (AbstractRelic relic : relics) {
                rewards.add(new RewardItem(relic));
            }
        });
    }
    
    public static void gainRelics(AbstractRelic... relics) {
        for (AbstractRelic relic : relics) {
            if (relic == null) {
                HSRMod.logger.error("RELIC_EVENT_HELPER: Null relic to gain.");
                continue;
            }
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
        }
    }

    public static void gainRelics(String... relicIDs) {
        for (String relicId : relicIDs) {
            RelicEventHelper.removeRelicFromPool(relicId);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), RelicLibrary.getRelic(relicId).makeCopy());
        }
    }
    
    /*public static void loseRelics(String... relicIDs) {
        loseRelics(Arrays.stream(relicIDs).map(RelicLibrary::getRelic).map(AbstractRelic::makeCopy).toArray(AbstractRelic[]::new));
    }*/

    public static void loseRelicsAfterwards(AbstractRelic... relics) {
        ModHelper.addEffectAbstract(() -> loseRelics(true, relics));
    }

    public static void loseRelics(String... relicIds) {
        loseRelics(true, Arrays.stream(relicIds).map(id -> AbstractDungeon.player.getRelic(id)).toArray(AbstractRelic[]::new));
    }

    public static void loseRelics(AbstractRelic... relics) {
        loseRelics(false, relics);
    }

    public static void loseRelics(boolean addRelicToPool, AbstractRelic... relics) {
        if (relics.length == 0) return;
        if (relics.length == 1) {
            AbstractDungeon.effectList.add(new BetterWarningSignEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, 4.0f));
            AbstractDungeon.player.loseRelic(relics[0].relicId);
            AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.4f, relics[0]));
            if (addRelicToPool) addRelicToPool(relics[0]);
        } else {
            for (int i = relics.length - 1; i >= 0; --i) {
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.effectList.add(new BetterWarningSignEffect(x, y, 4.0f));
                AbstractDungeon.player.loseRelic(relics[i].relicId);
                AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(x, y - 0.1f, relics[i]));
                if (addRelicToPool) addRelicToPool(relics[i]);
            }
        }
    }

    public static void gainGold(int amount) {
        if (AbstractDungeon.effectList.stream().noneMatch(e -> e instanceof RainingGoldEffect))
            AbstractDungeon.effectList.add(new RainingGoldEffect(Math.min(amount, 1000)));
        AbstractDungeon.player.gainGold(amount);
    }

    /**
     * Get a hsr relic of the specified tier and path.
     *
     * @param tier the tier of the relic
     * @return the relic's id
     */
    public static String getHSRRelic(AbstractRelic.RelicTier tier) {
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
        if (relics.isEmpty()) {
            return "";
        }
        String relic = relics.get(AbstractDungeon.relicRng.random(relics.size() - 1));
        pool.remove(relic);
        return relic;
    }

    public static AbstractRelic getRelicString(Predicate<AbstractRelic> predicate) {
        for (int j = 0; j < 99; ++j) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            if (!predicate.test(r) || r instanceof JellyfishOnTheStaircase) {
                addRelicToPool(r);
            } else {
                return r;
            }
        }
        return null;
    }

    /**
     * Get the path relic.
     *
     * @param tag the path tag
     * @return the path relic's id
     */
    public static String getRelicByPath(AbstractCard.CardTags tag) {
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
        if (tag == CustomEnums.ERUDITION) {
            relicName = TheWindSoaringValorous.ID;
        }
        if (tag == CustomEnums.TRAILBLAZE) {
            relicName = MasterOfDreamMachinations.ID;
        }
        if (!relicName.isEmpty()) {
            relicName = HSRMod.makePath(relicName);
        }
        return relicName;
    }

    public static void removeRelicFromPool(String relicID) {
        AbstractDungeon.commonRelicPool.remove(relicID);
        AbstractDungeon.uncommonRelicPool.remove(relicID);
        AbstractDungeon.rareRelicPool.remove(relicID);
    }

    public static void addRelicToPool(AbstractRelic relic) {
        switch (relic.tier) {
            case COMMON:
                AbstractDungeon.commonRelicPool.add(relic.relicId);
                break;
            case UNCOMMON:
                AbstractDungeon.uncommonRelicPool.add(relic.relicId);
                break;
            case RARE:
                AbstractDungeon.rareRelicPool.add(relic.relicId);
                break;
            case SHOP:
                AbstractDungeon.shopRelicPool.add(relic.relicId);
                break;
            case BOSS:
                AbstractDungeon.bossRelicPool.add(relic.relicId);
                break;
        }
    }
}
