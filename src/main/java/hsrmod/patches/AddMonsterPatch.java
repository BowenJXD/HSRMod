package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

import java.util.Collections;

public class AddMonsterPatch {
    @SpirePatch(clz = TheBeyond.class, method = "initializeBoss")
    public static class Act3AddBossPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            if (!Settings.isDailyRun && HSRMod.addEnemy) {
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
}
