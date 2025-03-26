package hsrmod.utils;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.BrokenPower;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

/**
 * Used to cache conditions within one single update, to avoid repeated calculations.
 * Realised based on CardCrawlGame.playtime.
 */
public class CachedCondition {
    
    public static HashMap<Key, BooleanSupplier> conditions = new HashMap<Key, BooleanSupplier>() {{
        put(Key.ANY_BROKEN, () -> {
            return AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.hasPower(BrokenPower.POWER_ID));
        });
    }};
    public static HashMap<Key, Boolean> cache = new HashMap<>();
    public static float cachedPlayTime;
    
    public static boolean check(Key key) {
        if (!conditions.containsKey(key)) {
            HSRMod.logger.error("CachedCondition: Key not found: {}", key);
            return false;
        }
        if (CardCrawlGame.playtime != cachedPlayTime) {
            cachedPlayTime = CardCrawlGame.playtime;
            conditions.forEach((k, v) -> cache.put(k, v.getAsBoolean()));
        }
        return cache.get(key);
    }
    
    public enum Key {
        ANY_BROKEN,
    }
}
