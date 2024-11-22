package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import hsrmod.characters.StellaCharacter;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

import java.util.Collections;

// TODO: Consider add boss from basemod.
public class AddMonsterPatch {
    @SpirePatch(clz = TheBeyond.class, method = "initializeBoss")
    public static class Act3AddBossPatch {
        @SpirePostfixPatch
        public static SpireReturn<Void> PostFix() {
            if (!Settings.isDailyRun && HSRMod.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRMod.removeOtherBosses) {
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
            if (!Settings.isDailyRun && HSRMod.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRMod.removeOtherBosses) {
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
            if (!Settings.isDailyRun && HSRMod.addEnemy && AbstractDungeon.player instanceof StellaCharacter) {
                if (HSRMod.removeOtherBosses) {
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
}
