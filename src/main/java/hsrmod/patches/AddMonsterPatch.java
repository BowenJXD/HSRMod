package hsrmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import hsrmod.characters.StellaCharacter;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

import java.util.*;
import java.util.stream.Collectors;

// TODO: Consider add boss from basemod.
public class AddMonsterPatch {
    static List<MonsterInfo> addMonsters(List<MonsterInfo> infos) {
        List<MonsterInfo> result = new ArrayList<>();
        if (HSRMod.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
            for (MonsterInfo info : infos) {
                result.add(new MonsterInfo(info.name, info.weight));
            }
        }
        return result;
    }

    @SpirePatch(clz = BaseMod.class, method = "getMonsterEncounters")
    public static class MonsterEncountersPatch {
        @SpirePostfixPatch
        public static List<MonsterInfo> PostFix(List<MonsterInfo> ___customMonsterEncounters) {
            return addMonsters(___customMonsterEncounters);
        }
    }

    @SpirePatch(clz = BaseMod.class, method = "getStrongMonsterEncounters")
    public static class StrongerMonsterEncountersPatch {
        @SpirePostfixPatch
        public static List<MonsterInfo> PostFix(List<MonsterInfo> ___customStrongMonsterEncounters) {
            return addMonsters(___customStrongMonsterEncounters);
        }
    }

    @SpirePatch(clz = BaseMod.class, method = "getEliteEncounters")
    public static class EliteEncountersPatch {
        @SpirePostfixPatch
        public static List<MonsterInfo> PostFix(List<MonsterInfo> ___customEliteEncounters) {
            return addMonsters(___customEliteEncounters);
        }
    }

    @SpirePatch(clz = BaseMod.class, method = "getBossIDs")
    public static class BossPatch {
        @SpirePostfixPatch
        public static List<String> PostFix(List<String> ___customBosses) {
            List<String> result = new ArrayList<>();
            if (!___customBosses.isEmpty() && HSRMod.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                result.addAll(___customBosses);
                if (HSRMod.removeOtherBosses) {
                    AbstractDungeon.bossList.clear();
                    AbstractDungeon.bossList.addAll(result);
                }
            }
            return result;
        }
    }
}
