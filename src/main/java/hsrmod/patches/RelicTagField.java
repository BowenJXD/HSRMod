package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(clz = AbstractRelic.class, method = SpirePatch.CLASS)
public class RelicTagField {
    public static SpireField<Boolean> destructible = new SpireField<>(new SpireField.DefaultValue<Boolean>() {
        @Override
        public Boolean get() {
            return false;
        }
    });
    public static SpireField<Boolean> subtle = new SpireField<>(new SpireField.DefaultValue<Boolean>() {
        @Override
        public Boolean get() {
            return false;
        }
    });
    public static SpireField<Boolean> economic = new SpireField<>(new SpireField.DefaultValue<Boolean>() {
        @Override
        public Boolean get() {
            return false;
        }
    });
}
