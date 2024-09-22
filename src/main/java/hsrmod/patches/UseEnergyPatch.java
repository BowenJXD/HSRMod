package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.subscribers.SubscribeManager;

@SpirePatch(clz = EnergyPanel.class, method = "useEnergy", paramtypez = {int.class})
public class UseEnergyPatch {
    @SpirePostfixPatch
    public static void useEnergy(int e) {
        SubscribeManager.getInstance().triggerPostEnergyChange(-e);
    }
}

