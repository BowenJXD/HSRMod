package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.powers.enemyOnly.SummonedPower;

import java.util.Objects;

@SpirePatch(clz = AbstractCreature.class, method = "hasPower")
public class PowerMapPatch {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> prefix(AbstractCreature _inst, String targetID) {
        if (Objects.equals(targetID, "Minion") && _inst.powers.stream().anyMatch(p -> Objects.equals(p.ID, SummonedPower.POWER_ID))) {
            return SpireReturn.Return(true);
        }
        return SpireReturn.Continue();
    }
}
