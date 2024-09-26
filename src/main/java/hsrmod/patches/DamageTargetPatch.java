package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageTargetPatch {

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class DamageTargetField {
        public static SpireField<AbstractCreature> target = new SpireField<>(() -> null);
    }

/*    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DamageTargetPatchMonster {
        @SpirePrefixPatch
        public static void setTarget(AbstractCreature _inst, DamageInfo info) {
            DamageTargetField.target.set(info, _inst);
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DamageTargetPatchPlayer {
        @SpirePrefixPatch
        public static void setTarget(AbstractCreature _inst, DamageInfo info) {
            DamageTargetField.target.set(info, _inst);
        }
    }*/
}
