package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CustomWrathParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private float dur_div2;
    private TextureAtlas.AtlasRegion img;
    float maxAlpha = 1F;

    public CustomWrathParticleEffect(Hitbox hb, Color color) {
        this.img = ImageMaster.GLOW_SPARK;
        this.duration = MathUtils.random(1.3F, 1.8F);
        this.scale = MathUtils.random(0.6F, 1.0F) * Settings.scale;
        this.dur_div2 = this.duration / 2.0F;
        this.color = color;
        if (color.a > 0) maxAlpha = color.a;
        this.x = hb.cX + MathUtils.random(-hb.width / 2.0F - 30.0F * Settings.scale, hb.width / 2.0F + 30.0F * Settings.scale);
        this.y = hb.cY + MathUtils.random(-hb.height / 2.0F - -10.0F * Settings.scale, hb.height / 2.0F - 10.0F * Settings.scale);
        this.x -= (float)this.img.packedWidth / 2.0F;
        this.y -= (float)this.img.packedHeight / 2.0F;
        this.renderBehind = MathUtils.randomBoolean(0.2F + (this.scale - 0.5F));
        this.rotation = MathUtils.random(-8.0F, 8.0F);
    }
    
    public CustomWrathParticleEffect(Hitbox hb) {
        this(hb, new Color(MathUtils.random(0.5F, 1.0F), 0.0F, MathUtils.random(0.0F, 0.2F), 0.6F));
    }

    public void update() {
        if (this.duration > this.dur_div2) {
            this.color.a = Interpolation.fade.apply(maxAlpha, 0.0F, (this.duration - this.dur_div2) / this.dur_div2);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, maxAlpha, this.duration / this.dur_div2);
        }

        this.vY += Gdx.graphics.getDeltaTime() * 40.0F * Settings.scale;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y + this.vY, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.8F, (0.1F + (this.dur_div2 * 2.0F - this.duration) * 2.0F * this.scale) * Settings.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
