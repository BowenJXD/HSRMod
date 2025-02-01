package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.TheCity.ShapeShifter;
import hsrmod.subscribers.SubscriptionManager;

@SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
public class MonsterDeathPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractMonster __instance) {
        SubscriptionManager.getInstance().triggerPostMonsterDeath(__instance);
    }
}
