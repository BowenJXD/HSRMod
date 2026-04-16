package hsrmod.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class CustomCleaveEffect extends CleaveEffect {
    public CustomCleaveEffect(Color color, float dx, float dy, float rotation) {
        super();
        this.color = color;
        this.rotation = rotation;
        try {
            float x = ReflectionHacks.getPrivate(this, CleaveEffect.class, "x");
            float y = ReflectionHacks.getPrivate(this, CleaveEffect.class, "y");
            ReflectionHacks.setPrivate(this, CleaveEffect.class, "x", x + dx);
            ReflectionHacks.setPrivate(this, CleaveEffect.class, "y", y + dy);
            ReflectionHacks.setPrivate(this, CleaveEffect.class, "vX", 400 * Settings.scale);
        } catch (Exception ignored) {
            
        }
    }
}
