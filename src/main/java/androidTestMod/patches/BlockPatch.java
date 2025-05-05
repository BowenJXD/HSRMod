package androidTestMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import androidTestMod.subscribers.SubscriptionManager;

public class BlockPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class})
    public static class BlockLossPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst, @ByRef int[] blockAmount) {
            blockAmount[0] = -SubscriptionManager.getInstance().triggerPreBlockChange(_inst, -blockAmount[0]);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class BlockGainPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst, @ByRef int[] blockAmount) {
            blockAmount[0] = SubscriptionManager.getInstance().triggerPreBlockChange(_inst, blockAmount[0]);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
    public static class BrokeBlockPatch {
        @SpirePrefixPatch
        public static void brokeBlock(AbstractCreature _inst) {
            SubscriptionManager.getInstance().triggerPostBreakBlock(_inst);
        }
    }
}
