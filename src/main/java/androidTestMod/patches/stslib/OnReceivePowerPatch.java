package androidTestMod.patches.stslib;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;
import javassist.CtBehavior;

public class OnReceivePowerPatch {
    public OnReceivePowerPatch() {
    }

    static SpireReturn<Void> CheckPower(AbstractGameAction action, AbstractCreature target, AbstractCreature source, float[] duration, AbstractPower powerToApply) {
        if (source != null && target != null) {
            for(AbstractPower power : target.powers) {
                if (power instanceof OnReceivePowerPower) {
                    action.amount = ((OnReceivePowerPower)power).onReceivePowerStacks(powerToApply, target, source, action.amount);
                    boolean apply = ((OnReceivePowerPower)power).onReceivePower(powerToApply, target, source);
                    if (!apply) {
                        AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(target, ApplyPowerAction.TEXT[0]));
                        duration[0] -= Gdx.graphics.getDeltaTime();
                        CardCrawlGame.sound.play("NULLIFY_SFX");
                        return SpireReturn.Return((Void) null);
                    }
                }
            }
        }

        return SpireReturn.Continue();
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPower {
        public ApplyPower() {
        }

        @SpireInsertPatch(
                locator = OnReceivePowerPatch.ApplyPower.Locator.class,
                localvars = {"duration", "powerToApply"}
        )
        public static SpireReturn<Void> Insert(ApplyPowerAction __instance, @ByRef float[] duration, AbstractPower powerToApply) {
            return OnReceivePowerPatch.CheckPower(__instance, __instance.target, __instance.source, duration, powerToApply);
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}

