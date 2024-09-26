package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.subscribers.SubscriptionManager;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock")
public class BlockGainPatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractCreature _inst, @ByRef int[] blockAmount) {
        blockAmount[0] = SubscriptionManager.getInstance().triggerPreBlockGain(_inst, blockAmount[0]);
    }
}
