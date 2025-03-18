/*package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Objects;

@SpirePatch(clz = AbstractPlayer.class, method = "hasRelic")
public class RelicMapPatch {
    @SpirePrefixPatch
    public SpireReturn<Boolean> prefix(AbstractPlayer _inst, String targetID) {
        if (Objects.equals(targetID, "WingedGreaves") && _inst.relics.stream().anyMatch((r) -> r.relicId.equals("WingedGreaves"))) {
            return SpireReturn.Return(true);
        }
        return SpireReturn.Continue();
    }
}*/
