package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.subscribers.SubscribeManager;

@SpirePatch(clz = EnergyPanel.class, method = "addEnergy", paramtypez = {int.class})
public class AddEnergyPatch {
    @SpirePrefixPatch
    public static void addEnergy(@ByRef int[] e) {
        e[0] = SubscribeManager.getInstance().triggerPreEnergyChange(e[0]);
    }
}
