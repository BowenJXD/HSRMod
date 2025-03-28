package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "updateTransparency")
public class TransparencyPatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractCard __instance) {
        if (__instance.fadingOut && __instance.targetTransparency == 1.0f) {
            __instance.fadingOut = false;
        }
    }
}
