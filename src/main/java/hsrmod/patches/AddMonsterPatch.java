package hsrmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import hsrmod.characters.StellaCharacter;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRModConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddMonsterPatch {
    static List<MonsterInfo> addMonsters(List<MonsterInfo> infos) {
        List<MonsterInfo> result = new ArrayList<>();
        if (HSRModConfig.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
            for (MonsterInfo info : infos) {
                result.add(new MonsterInfo(info.name, info.weight));
            }
        }
        return result;
    }

    @SpirePatch(clz = TheBeyond.class, method = "initializeBoss")
    public static class Act3AddBossPatch {
        @SpirePostfixPatch
        public static SpireReturn<Void> PostFix() {
            if (!Settings.isDailyRun && HSRModConfig.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRModConfig.hsrEnemyOnly) {
                    TheBeyond.bossList.clear();
                    TheBeyond.bossList.add(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
                    TheBeyond.bossList.add(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
                    TheBeyond.bossList.add(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
                } else {
                    TheBeyond.bossList.add(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
                    Collections.shuffle(TheBeyond.bossList, AbstractDungeon.monsterRng.random);
                    AbstractDungeon.bossList.remove(AbstractDungeon.bossList.size() - 1);
                }
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = TheCity.class, method = "initializeBoss")
    public static class Act2AddBossPatch {
        @SpirePostfixPatch
        public static SpireReturn<Void> PostFix() {
            if (!Settings.isDailyRun && HSRModConfig.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRModConfig.hsrEnemyOnly) {
                    TheCity.bossList.clear();
                    TheCity.bossList.add(Encounter.DIVINE_SEED);
                    TheCity.bossList.add(Encounter.DIVINE_SEED);
                    TheCity.bossList.add(Encounter.DIVINE_SEED);
                } else {
                    TheCity.bossList.add(Encounter.DIVINE_SEED);
                    Collections.shuffle(TheCity.bossList, AbstractDungeon.monsterRng.random);
                    AbstractDungeon.bossList.remove(AbstractDungeon.bossList.size() - 1);
                }
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = Exordium.class, method = "initializeBoss")
    public static class Act1AddBossPatch {
        @SpirePostfixPatch
        public static SpireReturn<Void> PostFix() {
            if (!Settings.isDailyRun && HSRModConfig.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRModConfig.hsrEnemyOnly) {
                    Exordium.bossList.clear();
                    Exordium.bossList.add(Encounter.END_OF_THE_ETERNAL_FREEZE);
                    Exordium.bossList.add(Encounter.END_OF_THE_ETERNAL_FREEZE);
                    Exordium.bossList.add(Encounter.END_OF_THE_ETERNAL_FREEZE);
                } else {
                    Exordium.bossList.add(Encounter.END_OF_THE_ETERNAL_FREEZE);
                    Collections.shuffle(Exordium.bossList, AbstractDungeon.monsterRng.random);
                    AbstractDungeon.bossList.remove(AbstractDungeon.bossList.size() - 1);
                }
            }
            return SpireReturn.Return();
        }
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
}