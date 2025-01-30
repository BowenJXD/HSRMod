package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.subscribers.SubscriptionManager;

public class OnRelicDestroyPatch {
    @SpirePatch(clz = AbstractRelic.class, method = "setCounter")
    public static class SetCounterPatch {
        @SpirePostfixPatch
        public static void PostFix(AbstractRelic __instance, int counter) {
            if (counter == -2) {
                SubscriptionManager.getInstance().triggerPostRelicDestroy(__instance);
            }
        }
    }
}
