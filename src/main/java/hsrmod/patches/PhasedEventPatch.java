package hsrmod.patches;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PhasedEventPatch {
    @SpirePatch(clz = TextPhase.OptionInfo.class, method = "setOptionResult", paramtypez = {Consumer.class})
    public static class SetOptionResult {
        public static SpireReturn<TextPhase.OptionInfo> Prefix(TextPhase.OptionInfo __instance, Consumer<Integer> optionResult) {
            if (optionResult != null) {
                try {
                    __instance.optionResult = __instance.optionResult.andThen((event, i) -> optionResult.accept(i));
                } catch (Exception e) {
                    return SpireReturn.Continue();
                }
                return SpireReturn.Return(__instance);
            }
            return SpireReturn.Continue();
        }
    }
    
    @SpirePatch(clz = TextPhase.OptionInfo.class, method = "setOptionResult", paramtypez = {BiConsumer.class})
    public static class SetOptionResultBi {
        public static SpireReturn<TextPhase.OptionInfo> Prefix(TextPhase.OptionInfo __instance, BiConsumer<PhasedEvent, Integer> optionResult) {
            if (optionResult != null) {
                try {
                    __instance.optionResult = __instance.optionResult.andThen(optionResult);
                } catch (Exception e) {
                    return SpireReturn.Continue();
                }
                return SpireReturn.Return(__instance);
            }
            return SpireReturn.Continue();
        }
    }
}
