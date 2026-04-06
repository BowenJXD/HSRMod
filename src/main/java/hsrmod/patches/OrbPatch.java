package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DarkOrbEvokeAction;
import com.megacrit.cardcrawl.actions.defect.LightningOrbEvokeAction;
import com.megacrit.cardcrawl.actions.defect.LightningOrbPassiveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.SubscriptionManager;
import javassist.CtBehavior;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrbPatch {

    @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
    @SpirePatch(clz = Lightning.class, method = "onEndOfTurn")
    @SpirePatch(clz = Dark.class, method = "onEndOfTurn")
    @SpirePatch(clz = Plasma.class, method = "onStartOfTurn")
    public static class OrbEndOfTurnPatch {
        static Integer amountCache = null; 
        
        public static void Prefix(AbstractOrb __inst) {
            amountCache = __inst.passiveAmount;
            __inst.passiveAmount = SubscriptionManager.getInstance().triggerPreOrbPassive(__inst, __inst.passiveAmount);
        }
        
        public static void Postfix(AbstractOrb __inst) {
            __inst.passiveAmount = amountCache;
        }
    }
    
    @SpirePatch(clz = AbstractOrb.class, method = "onEvoke")
    public static class OrbEvokePatch {
        static Integer amountCache = null;

        public static void Prefix(AbstractOrb __inst) {
            amountCache = __inst.evokeAmount;
            __inst.evokeAmount = SubscriptionManager.getInstance().triggerPreOrbEvoke(__inst, __inst.evokeAmount);
        }
        
        public static void Postfix(AbstractOrb __inst) {
            __inst.evokeAmount = amountCache;
        }
    }
    
    @SpirePatch(clz = LightningOrbEvokeAction.class, method = "update")
    public static class LightningOrbEvokeActionPatch {
        static int previousSize = 0;

        public static void Prefix(LightningOrbEvokeAction __inst) {
            previousSize = AbstractDungeon.actionManager.actions.size();
        }

        public static void Postfix(LightningOrbEvokeAction __inst, DamageInfo ___info) {
            ProcessDamage(___info, previousSize);
        }
    }

    @SpirePatch(clz = LightningOrbPassiveAction.class, method = "update")
    public static class LightningOrbPassiveActionPatch {
        static int previousSize = 0;

        public static void Prefix(LightningOrbPassiveAction __inst) {
            previousSize = AbstractDungeon.actionManager.actions.size();
        }

        public static void Postfix(LightningOrbPassiveAction __inst, DamageInfo ___info) {
            ProcessDamage(___info, previousSize);
        }
    }

    private static void ProcessDamage(DamageInfo ___info, int previousSize) {
        for (int i = 0; i < AbstractDungeon.actionManager.actions.size() - previousSize; i++) {
            AbstractGameAction action = AbstractDungeon.actionManager.actions.get(AbstractDungeon.actionManager.actions.size() - 1 - i);
            if (action instanceof DamageAction) {
                DamageAction damageAction = (DamageAction) action;
                AbstractDungeon.actionManager.actions.set(i, new ElementalDamageAction(
                        damageAction.target,
                        new ElementalDamageInfo(
                                ___info.owner,
                                ___info.output,
                                ___info.type,
                                ElementType.Lightning,
                                1
                        ),
                        damageAction.attackEffect
                ));
            } else if (action instanceof DamageAllEnemiesAction) {
                DamageAllEnemiesAction damageAllEnemiesAction = (DamageAllEnemiesAction) action;
                AbstractDungeon.actionManager.actions.set(i, new ElementalDamageAllAction(
                        damageAllEnemiesAction.source,
                        damageAllEnemiesAction.damage,
                        damageAllEnemiesAction.damageType,
                        ElementType.Lightning,
                        1,
                        damageAllEnemiesAction.attackEffect
                ));
            }
        }
    }

    @SpirePatch(clz = DarkOrbEvokeAction.class, method = "update")
    public static class DarkOrbEvokeActionPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Insert(DarkOrbEvokeAction __inst, DamageInfo ___info) {
            AbstractDungeon.actionManager.addToTop(new ElementalDamageAction(
                            __inst.target,
                            new ElementalDamageInfo(
                                    ___info.owner,
                                    ___info.base,
                                    ___info.type,
                                    ElementType.Quantum,
                                    1),
                            __inst.attackEffect
                    )
            );

            return SpireReturn.Return();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(
                        AbstractCreature.class, "damage"
                );
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), matcher);
            }
        }
    }
}
