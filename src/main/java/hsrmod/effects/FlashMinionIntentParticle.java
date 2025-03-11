package hsrmod.effects;

import hsrmod.minion.AbstractPlayerMinion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashMinionIntentParticle extends AbstractGameEffect {
    private static final float DURATION = 1.0F;
    private static final float START_SCALE;
    private float scale = 0.01F;
    private static int W;
    private final Texture img;
    private final float x;
    private final float y;
    private final Color shineColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);

    public FlashMinionIntentParticle(Texture img, AbstractPlayerMinion m) {
        this.duration = 1.0F;
        this.img = img;
        W = img.getWidth();
        this.x = m.intentHb.cX - (float)W / 2.0F;
        this.y = m.intentHb.cY - (float)W / 2.0F;
        this.renderBehind = true;
    }

    public void update() {
        this.scale = Interpolation.fade.apply(START_SCALE, 0.01F, this.duration);
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb, float x, float y) {
    }

    public void dispose() {
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        this.shineColor.a = this.duration / 2.0F;
        sb.setColor(this.shineColor);
        sb.draw(this.img, this.x, this.y, (float)W / 2.0F, (float)W / 2.0F, (float)W, (float)W, this.scale, this.scale, 0.0F, 0, 0, W, W, false, false);
        sb.setBlendFunction(770, 771);
    }

    static {
        START_SCALE = 5.0F * Settings.scale;
    }
}
