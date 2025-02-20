package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.mod.stslib.util.Grayscale;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// 灰度滤镜效果：在 1 秒内先从正常画面过渡到全灰，再恢复到正常画面
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class GrayscaleScreenEffect extends AbstractGameEffect {
    private final float startingDuration;
    private FrameBuffer frameBuffer;
    private TextureRegion fboRegion;
    private ShaderProgram shader;

    public GrayscaleScreenEffect(float duration) {
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.duration = this.startingDuration = duration;
        this.frameBuffer = frameBuffer;
        this.fboRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.fboRegion.flip(false, true);  // 修正 y 坐标翻转
        initShader();
    }

    // 初始化着色器程序
    private void initShader() {
        // 使用已有的着色器代码
        if (!Grayscale.program.isCompiled()) {
            Gdx.app.error("GrayscaleEffect", "Shader compilation failed: " + Grayscale.program.getLog());
        }
        shader = Grayscale.program;
    }

    // 更新效果的持续时间
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    // 渲染灰度效果
    public void render(SpriteBatch sb) {
        // 计算灰度强度（前半段从 0 -> 1，后半段从 1 -> 0）
        float halfDuration = startingDuration / 2f;
        float intensity;
        if (duration > halfDuration) {
            // 前半段：强度从 0 渐变到 1
            intensity = Interpolation.fade.apply(0f, 1f, (startingDuration - duration) / halfDuration);
        } else {
            // 后半段：强度从 1 渐变回 0
            intensity = Interpolation.fade.apply(1f, 0f, (halfDuration - duration) / halfDuration);
        }

        // 设置着色器的强度
        sb.setShader(shader);
        // shader.setUniformf("intensity", intensity);
        // 绘制当前屏幕的 FrameBuffer 内容
        sb.draw(fboRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 恢复默认着色器
        sb.setShader(null);
    }

    public void dispose() {
        // 可以释放不再需要的资源，例如 ShaderProgram
        if (shader != null) {
            shader.dispose();
        }
    }
}

