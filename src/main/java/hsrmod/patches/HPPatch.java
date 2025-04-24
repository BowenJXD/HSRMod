package hsrmod.patches;

import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
    
    @SpirePatch(clz = PlayerDamage.class, method = "Insert", paramtypez = {AbstractCreature.class, DamageInfo.class, int[].class, boolean[].class})
    public static class LoseTempHPPatch {
        @SpireInsertPatch(rloc = 13)
        public static void Insert(AbstractCreature __instance) {
            SubscriptionManager.getInstance().triggerPostHPUpdate(__instance);
        }
    }
}
