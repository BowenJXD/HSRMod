package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.subscribers.SubscribeManager;

@SpirePatch(clz = EnergyPanel.class, method = "addEnergy", paramtypez = {int.class})
public class AddEnergyPatch {
    @SpirePostfixPatch
    public static void addEnergy(int e) {
        SubscribeManager.getInstance().triggerPostEnergyChange(e);
    }
}
