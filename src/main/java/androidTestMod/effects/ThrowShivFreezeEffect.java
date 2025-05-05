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

public class ThrowShivFreezeEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float destY;
    public static final float FULL_DURATION = 0.4F;
    private TextureAtlas.AtlasRegion img;
    private boolean playedSound = false;

    // 原效果的构造器
    public ThrowShivFreezeEffect(float baseX, float baseY, Color color, boolean playSound) {
        this.img = ImageMaster.DAGGER_STREAK;
        // 根据传入的基准位置计算最终显示位置
        this.x = baseX - MathUtils.random(320.0F, 360.0F) - (float) this.img.packedWidth / 2.0F;
        this.destY = baseY;
        this.y = this.destY + MathUtils.random(-25.0F, 25.0F) * Settings.scale - (float) this.img.packedHeight / 2.0F;
        this.startingDuration = FULL_DURATION;
        this.duration = FULL_DURATION;
        this.scale = Settings.scale * MathUtils.random(0.5F, 2.0F); 
        this.rotation = MathUtils.random(-30.0F, 30.0F);
        this.color = color;
        this.playedSound = !playSound;
    }

    // 重构用的构造器：利用记录的参数构建，仅播放后半段动画（0.2秒）
    public ThrowShivFreezeEffect(ShivParams params) {
        this.img = ImageMaster.DAGGER_STREAK;
        this.x = params.x;
        this.y = params.y;
        this.destY = params.destY;
        this.startingDuration = 0.2F;
        this.duration = 0.2F;
        this.scale = params.scale;
        this.rotation = params.rotation;
        this.color = new Color(params.color);
        // 已经播放声音的效果不再重复播放
        this.playedSound = true;
    }

    public void update() {
        if (!this.playedSound) {
            this.playRandomSfX();
            this.playedSound = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration > 0.2F) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 0.2F) * 5.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration * 5.0F);
        }

        this.scale = Interpolation.bounceIn.apply(Settings.scale * 0.5F, Settings.scale * 1.5F, this.duration / 0.4F);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth * 0.85F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale * 1.5F, this.rotation);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth * 0.85F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 0.75F, this.scale * 0.75F, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    private void playRandomSfX() {
        int roll = MathUtils.random(5);
        switch (roll) {
            case 0:
                CardCrawlGame.sound.play("ATTACK_DAGGER_1");
                break;
            case 1:
                CardCrawlGame.sound.play("ATTACK_DAGGER_2");
                break;
            case 2:
                CardCrawlGame.sound.play("ATTACK_DAGGER_3");
                break;
            case 3:
                CardCrawlGame.sound.play("ATTACK_DAGGER_4");
                break;
            case 4:
                CardCrawlGame.sound.play("ATTACK_DAGGER_5");
                break;
            default:
                CardCrawlGame.sound.play("ATTACK_DAGGER_6");
        }
    }

    public void dispose() {
    }

    // 内部类，用于记录构造时的参数
    public class ShivParams {
        public float x;
        public float y;
        public float destY;
        public float rotation;
        public float scale;
        public Color color;

        public ShivParams(float x, float y, float destY, float rotation, float scale, Color color) {
            this.x = x;
            this.y = y;
            this.destY = destY;
            this.rotation = rotation;
            this.scale = scale;
            this.color = new Color(color);
        }
    }

    // 获取当前效果的记录参数
    public ShivParams getShivParams() {
        return new ShivParams(this.x, this.y, this.destY, this.rotation, this.scale, this.color);
    }
}
