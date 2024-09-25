package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.subscribers.SubscribeManager;

@SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
public class BrokeBlockPatch {
    @SpirePrefixPatch
    public static void brokeBlock(AbstractCreature _inst) {
        SubscribeManager.getInstance().triggerPostBreakBlock(_inst);
    }
}
