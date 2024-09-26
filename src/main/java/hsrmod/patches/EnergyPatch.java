package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.subscribers.SubscriptionManager;

public class EnergyPatch {
    @SpirePatch(clz = EnergyPanel.class, method = "addEnergy", paramtypez = {int.class})
    public static class AddPatch {
        @SpirePrefixPatch
        public static void addEnergy(@ByRef int[] e) {
            e[0] = SubscriptionManager.getInstance().triggerPreEnergyChange(e[0]);
        }
    }
    
    @SpirePatch(clz = EnergyPanel.class, method = "useEnergy", paramtypez = {int.class})
    public static class UsePatch {
        @SpirePrefixPatch
        public static void useEnergy(@ByRef int[] e) {
            e[0] = -SubscriptionManager.getInstance().triggerPreEnergyChange(-e[0]);
        }
    }
}
