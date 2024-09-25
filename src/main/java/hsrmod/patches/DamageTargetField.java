package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class DamageTargetField {
    public static SpireField<AbstractCreature> target = new SpireField<>(() -> null);
}
