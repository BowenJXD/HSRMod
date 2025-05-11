package hsrmod.patches;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicTagPatch {
    @SpirePatch(clz = CustomRelic.class, method = "makeCopy")
    public static class RelicTagCopyPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractRelic> Prefix(CustomRelic _inst) {
            AbstractRelic result;
            try {
                result = (AbstractRelic)_inst.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("BaseMod failed to auto-generate makeCopy for relic: " + _inst.relicId);
            }
            if (RelicTagField.destructible.get(_inst)) {
                RelicTagField.destructible.set(result, true);
            }
            if (RelicTagField.subtle.get(_inst)) {
                RelicTagField.subtle.set(result, true);
            }
            if (RelicTagField.economic.get(_inst)) {
                RelicTagField.economic.set(result, true);
            }
            return SpireReturn.Return(result);
        }
    }
}
