package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import hsrmod.effects.UnlockEffect;
import hsrmod.misc.IHSRCharacter;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.utils.GeneralUtil;

public class VictoryPatch {
    static final UIStrings uiStrings;
    static final String[] TEXT;
    
    @SpirePatch(clz = VictoryScreen.class, method = "updateAscensionAndBetaArtProgress")
    public static class VictoryScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(VictoryScreen _inst) {
            if (!PathSelectScreen.Inst.unlockedAll()) {
                AbstractDungeon.topLevelEffects.add(new UnlockEffect(TEXT[0]));
                PathSelectScreen.Inst.setPathUnlocked();
            } else if (AbstractDungeon.ascensionLevel == Settings.MAX_ASCENSION_LEVEL
                    && AbstractDungeon.player instanceof IHSRCharacter
                    && HSRModConfig.tpLimit < HSRModConfig.TP_LIMIT_LIMIT) {
                if (HSRModConfig.tpLimit == 0) {
                    AbstractDungeon.topLevelEffects.add(new UnlockEffect(TEXT[1]));
                    HSRModConfig.addTPLimit();
                } else if (HSRModConfig.getActiveTPCount() == HSRModConfig.tpLimit) {
                    AbstractDungeon.topLevelEffects.add(new UnlockEffect(GeneralUtil.tryFormat(TEXT[2], HSRModConfig.tpLimit + 1)));
                    HSRModConfig.addTPLimit();
                }
            }
        }
    }
    
    @SpirePatch(clz = DeathScreen.class, method = "updateAscensionProgress")
    public static class DeathScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(DeathScreen _inst) {
            if (!PathSelectScreen.Inst.unlockedAll() && GameOverScreen.isVictory) {
                AbstractDungeon.topLevelEffects.add(new UnlockEffect(TEXT[0]));
                PathSelectScreen.Inst.setPathUnlocked();
            }
        }
    }
    
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(HSRMod.makePath(VictoryPatch.class.getSimpleName()));
        TEXT = uiStrings.TEXT;
    }
}
