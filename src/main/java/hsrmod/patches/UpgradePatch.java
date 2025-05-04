package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.subscribers.SubscriptionManager;

public class UpgradePatch {
    @SpirePatch(clz = AbstractCard.class, method = "superFlash", paramtypez = {})
    public static class PostUpgradePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __inst) {
            if (__inst != null && __inst.upgraded)
                SubscriptionManager.getInstance().triggerPostUpgrade(__inst);
        }
    }
}
