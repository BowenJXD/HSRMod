package androidTestMod.utils;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.misc.BrokenPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Used to cache conditions within one single update, to avoid repeated calculations.
 * Realised based on CardCrawlGame.playtime.
 */
public class CachedCondition {
    
    public static HashMap<Key, BooleanSupplier> conditions = new HashMap<Key, BooleanSupplier>() {{
        put(Key.ANY_BROKEN, new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m.hasPower(BrokenPower.POWER_ID)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }};
    public static HashMap<Key, Boolean> cache = new HashMap<>();
    public static float cachedPlayTime;
    
    public static boolean check(Key key) {
        if (!conditions.containsKey(key)) {
            AndroidTestMod.logger.error("CachedCondition: Key not found: {}", key);
            return false;
        }
        if (CardCrawlGame.playtime != cachedPlayTime) {
            cachedPlayTime = CardCrawlGame.playtime;
            for (Map.Entry<Key, BooleanSupplier> entry : conditions.entrySet()) {
                Key k = entry.getKey();
                BooleanSupplier v = entry.getValue();
                cache.put(k, v.getAsBoolean());
            }
        }
        return cache.get(key);
    }
    
    public enum Key {
        ANY_BROKEN,
    }
}
