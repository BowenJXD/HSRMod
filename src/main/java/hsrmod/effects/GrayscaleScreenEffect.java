package hsrmod.effects;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.mod.stslib.util.Grayscale;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// 灰度滤镜效果：在 1 秒内先从正常画面过渡到全灰，再恢复到正常画面
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class GrayscaleScreenEffect extends AbstractGameEffect {
    private final float startingDuration;
    ScreenPostProcessor grayscalePostProcessor;

    public GrayscaleScreenEffect(float duration) {
        this.duration = this.startingDuration = duration;
        grayscalePostProcessor = new GrayscaleScreenPostProcessor();
    }

    // 更新效果的持续时间
    public void update() {
        if (duration == startingDuration) {
            ScreenPostProcessorManager.addPostProcessor(grayscalePostProcessor);
        }
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
        @Override
        public void postProcess(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera camera) {
            // 设置灰度效果的强度
            sb.setShader(Grayscale.program);

            // 渲染屏幕内容
            sb.setColor(Color.WHITE);
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

