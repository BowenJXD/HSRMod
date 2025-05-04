package hsrmod.effects;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GrayscaleScreenEffect extends AbstractGameEffect {
    private final float startingDuration;
    GrayscaleScreenPostProcessor grayscalePostProcessor;
    float fadeInTime = 0.5F;
    float fadeOutTime = 0.5F;

    public GrayscaleScreenEffect(float duration) {
        this.duration = this.startingDuration = duration;
        grayscalePostProcessor = new GrayscaleScreenPostProcessor();
        this.color = Color.WHITE.cpy();
    }

    // 更新效果的持续时间
    public void update() {
        if (duration == startingDuration) {
            ScreenPostProcessorManager.addPostProcessor(grayscalePostProcessor);
        }
        float intensity = 0;
        if (duration > startingDuration - fadeInTime)
            intensity = Interpolation.fade.apply(0.0F, 1.0F, (startingDuration - duration) / fadeInTime);
        else if (duration < fadeOutTime)
            intensity = Interpolation.fade.apply(0.0F, 1.0F, duration / fadeOutTime);
        else
            intensity = 1.0F;
        grayscalePostProcessor.intensity = intensity;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            ScreenPostProcessorManager.removePostProcessor(grayscalePostProcessor);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void dispose() {

    }

    public static class GrayscaleScreenPostProcessor implements ScreenPostProcessor {
        public Color color = Color.WHITE.cpy();
        public float intensity = 0;
        public float hueThreshold = 15;
        
        public GrayscaleScreenPostProcessor() {
        }
        
        @Override
        public void postProcess(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera camera) {
            // 设置灰度效果的强度
            sb.setShader(GrayscaleButRedShader.program);

            // 渲染屏幕内容
            sb.setColor(color);
            GrayscaleButRedShader.program.setUniformf("intensity", intensity);
            GrayscaleButRedShader.program.setUniformf("hueThreshold", hueThreshold);
            sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
            sb.draw(textureRegion, 0, 0);  // 渲染当前帧的纹理

            // 恢复默认设置
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sb.setShader(null);
        }

        @Override
        public int priority() {
            return 50;  // 设置处理优先级
        }
    }
}

