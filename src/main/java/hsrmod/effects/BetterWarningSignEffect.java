package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BetterWarningSignEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float scale;

    public BetterWarningSignEffect(float x, float y, float scale) {
        this.duration = 1.0F;
        this.color = Color.SCARLET.cpy();
        this.color.a = 0.0F;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public void update() {
        if (1.0F - this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 10.0F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(ImageMaster.WARNING_ICON_VFX, this.x - 32.0F, this.y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * scale, Settings.scale * scale, 0.0F, 0, 0, 64, 64, false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
