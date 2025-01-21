package hsrmod.utils;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.boss.*;
import hsrmod.relics.special.*;
import hsrmod.relics.special.TheWindSoaringValorous;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicEventHelper {
    public static void upgradeCards(int amount) {
        int count = 0;
        List<String> upgradedCards = new ArrayList<>();
        List<AbstractCard> list = AbstractDungeon.player.masterDeck.group;
        Collections.shuffle(list, AbstractDungeon.relicRng.random);

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
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
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
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
            }
        }
    }
    
    public static void gainRelicsAfterwards(int amount) {
        ModHelper.addEffectAbstract(() -> gainRelics(amount));
    }
    
    public static void gainRelics(int amount){
        gainRelics(amount, AbstractDungeon.returnRandomRelicTier());
    }
    
    public static void gainRelics(int amount, AbstractRelic.RelicTier tier) {
        gainRelics(amount, tier, r -> !RelicTagField.subtle.get(r));
    }
    
    public static void gainRelics(int amount, Predicate<AbstractRelic> predicate) {
        gainRelics(amount, AbstractDungeon.returnRandomRelicTier(), predicate);
    }
    
    public static void gainRelics(int amount, AbstractRelic.RelicTier tier, Predicate<AbstractRelic> predicate) {
        for (int i = 0, j = 0; i < amount && j < 99; ++i, ++j) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(tier);
            if (!predicate.test(r)) {
                --i;
                addRelicToPool(r);
            } else {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
            }
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
        ModHelper.addEffectAbstract(() -> loseRelics(relics));
    }
    
    public static void loseRelics(AbstractRelic... relics) {
        if (relics.length == 0) return;
        if (relics.length == 1) {
            AbstractDungeon.effectList.add(new BetterWarningSignEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, 4.0f));
            AbstractDungeon.player.loseRelic(relics[0].relicId);
            AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.4f, relics[0]));
        } else {
            for (int i = relics.length - 1; i >= 0; --i) {
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.effectList.add(new BetterWarningSignEffect(x, y, 4.0f));
                AbstractDungeon.player.loseRelic(relics[i].relicId);
                AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(x, y - 0.1f, relics[i]));
            }
        }
    }
    
    public static void gainGold(int amount) {
        AbstractDungeon.effectList.add(new RainingGoldEffect(amount));
        AbstractDungeon.player.gainGold(amount);
    }

    /**
     * Get a hsr relic of the specified tier and path.
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

    /**
     * Get the path relic.
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
