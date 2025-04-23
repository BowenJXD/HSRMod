package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.powers.enemyOnly.SummonedPower;

import java.util.Objects;

public class PowerPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "hasPower")
    public static class PowerMapPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> prefix(AbstractCreature _inst, String targetID) {
            if (Objects.equals(targetID, "Minion") && _inst.powers.stream().anyMatch(p -> Objects.equals(p.ID, SummonedPower.POWER_ID))) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ReducePowerAction.class, method = "update")
    public static class ReducePowerActionPatch {

        public static Integer cache = null;

        @SpireInsertPatch(rloc = 9, localvars = {"reduceMe"})
        public static void Insert(ReducePowerAction __inst, AbstractPower reduceMe) {
            if (reduceMe.canGoNegative) {
                cache = __inst.amount;
                __inst.amount = -9999;
            }
        }

        @SpireInsertPatch(rloc = 10)
        public static void Insert(ReducePowerAction __inst) {
            if (cache != null)
                __inst.amount = cache;
            cache = null;
        }
    }
}
