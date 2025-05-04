package hsrmod.patches.stslib;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.interfaces.ClickableRelic;
import javassist.CtBehavior;

@SpirePatch(
        clz = OverlayMenu.class,
        method = "update"
)
public class ClickableRelicUpdatePatch {
    public ClickableRelicUpdatePatch() {
    }

    @SpireInsertPatch(
            locator = ClickableRelicUpdatePatch.Locator.class,
            localvars = {"r"}
    )
    public static void Insert(OverlayMenu __instance, AbstractRelic relic) {
        if (relic instanceof ClickableRelic) {
            ((ClickableRelic)relic).clickUpdate();
        }

    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "update");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}

