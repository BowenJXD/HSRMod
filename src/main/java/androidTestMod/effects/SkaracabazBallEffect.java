package androidTestMod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SkaracabazBallEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private TextureAtlas.AtlasRegion[] frames;
    private float x, sX, tX;
    private float y, sY, tY;
    float shakeIntensity = 2.0F;
    
    public SkaracabazBallEffect(float sX, float sY, float tX, float tY) {
        this.duration = startingDuration = Settings.FAST_MODE ? 1.2F : 2.4F;
        this.color = Color.WHITE.cpy();
        
        frames = new TextureAtlas.AtlasRegion[6];
        for (int i = 1; i <= frames.length; i++) {
            frames[i-1] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("img/effects/Skaracabaz/" + i + ".png"), 0, 0, 870, 870);
        }
        img = frames[0];
        
        this.x = this.sX = sX - (float)this.img.packedWidth / 2.0F;
        this.y = this.sY = sY - (float)this.img.packedHeight / 2.0F;
        this.tX = tX - (float)this.img.packedWidth / 2.0F;
        this.tY = tY - (float)this.img.packedHeight / 2.0F;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.playA("HEART_BEAT", MathUtils.random(0.0F, 0.6F));
        }
        
        int index = (int) (duration * frames.length / startingDuration);
        if (index >= frames.length) {
            index = frames.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        img = frames[index];

        if (duration / startingDuration > 0.1)
            scale = Interpolation.exp5Out.apply(0.6F, 0F, duration / startingDuration);
        else {
            scale = Interpolation.exp5In.apply(2F, 0.5F, duration * 10 / startingDuration);
            color.a = Interpolation.exp5In.apply(0, 1, duration * 10 / startingDuration);
        }
        x = Interpolation.exp5In.apply(tX, sX, duration / startingDuration);
        y = Interpolation.exp5In.apply(tY, sY, duration / startingDuration);

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        float currX = this.x + MathUtils.random(-shakeIntensity, shakeIntensity);
        float currY = this.y + MathUtils.random(-shakeIntensity, shakeIntensity);
        sb.draw(this.img, currX, currY, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, (this.scale + MathUtils.random(-0.02F, 0.02F)) * 3.0F, (this.scale + MathUtils.random(-0.03F, 0.03F)) * 3.0F, this.rotation);
        sb.draw(this.img, currX, currY, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, (this.scale + MathUtils.random(-0.02F, 0.02F)) * 3.0F, (this.scale + MathUtils.random(-0.03F, 0.03F)) * 3.0F, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
