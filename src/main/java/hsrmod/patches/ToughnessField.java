package hsrmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
public class ToughnessField {
    public static SpireField<Integer> toughness = new SpireField<>(() -> 0);
}

