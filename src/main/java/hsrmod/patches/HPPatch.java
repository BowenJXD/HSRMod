package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.subscribers.SubscriptionManager;

public class HPPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent")
    public static class HPBarUpdatePatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance) {
            SubscriptionManager.getInstance().triggerPostHPUpdate(__instance);
        }
    }
    
    @SpirePatch(clz = LoseHPAction.class, method = "update")
    public static class LoseHPPatch{
        @SpirePostfixPatch
        public static void Postfix(LoseHPAction __instance) {
            if (__instance.isDone && __instance.target != null) {
                SubscriptionManager.getInstance().triggerPostHPUpdate(__instance.target);
            }
        }
    }
}
