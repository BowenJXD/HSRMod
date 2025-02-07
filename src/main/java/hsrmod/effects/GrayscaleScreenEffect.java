package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// 灰度滤镜效果：在 1 秒内先从正常画面过渡到全灰，再恢复到正常画面
public class GrayscaleScreenEffect extends AbstractGameEffect {
    private final float startingDuration;
    private ShaderProgram shader; // 自定义 shader 用于颜色向灰度转换

    public GrayscaleScreenEffect(float duration) {
        this.startingDuration = duration;  // 总时长 1 秒，可根据需要调整
        this.duration = startingDuration;
        this.renderBehind = false;
        initShader();
    }

    // 初始化自定义 shader
    private void initShader() {
        String vertexShader =
                "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                        "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                        "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
                        "uniform mat4 u_projTrans;\n" +
                        "varying vec4 v_color;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "\n" +
                        "void main() {\n" +
                        "    v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                        "    v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
                        "    gl_Position = u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                        "}";
        String fragmentShader =
                "#ifdef GL_ES\n" +
                        "precision mediump float;\n" +
                        "#endif\n" +
                        "varying vec4 v_color;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "uniform sampler2D u_texture;\n" +
                        "uniform float intensity; // 0：原色，1：完全灰度\n" +
                        "\n" +
                        "void main() {\n" +
                        "    vec4 color = texture2D(u_texture, v_texCoords) * v_color;\n" +
                        "    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));\n" +
                        "    vec3 grayscaleColor = vec3(gray);\n" +
                        "    // mix(原色, 灰度色, intensity) 当 intensity=1 时为全灰；为 0 时为原色\n" +
                        "    gl_FragColor = vec4(mix(color.rgb, grayscaleColor, intensity), color.a);\n" +
                        "}";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.error("GrayscaleScreenEffect", "Shader compilation failed:\n" + shader.getLog());
        }
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        // 计算灰度滤镜的强度：前半段（1.0->0.5秒）从 0 渐变到 1，后半段从 1 渐变回 0
        float halfDuration = startingDuration / 2.0F;
        float intensity;
        if (this.duration > halfDuration) {
            // 前半段：当 duration 从 1.0 递减到 0.5 时，(startingDuration - duration) 从 0 渐变到 0.5
            intensity = Interpolation.fade.apply(0.0F, 1.0F, (startingDuration - this.duration) / halfDuration);
        } else {
            // 后半段：当 duration 从 0.5 递减到 0 时，(halfDuration - duration) 从 0 渐变到 0.5
            intensity = Interpolation.fade.apply(1.0F, 0.0F, (halfDuration - this.duration) / halfDuration);
        }

        // 设置 shader
        sb.setShader(shader);
        shader.setUniformf("intensity", intensity);

        // 绘制全屏白色矩形（借助混合色与 shader 来对屏幕内容进行灰度处理）
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                0, 0,             // 左下角坐标
                Settings.WIDTH,   // 宽度覆盖整个屏幕
                Settings.HEIGHT); // 高度覆盖整个屏幕

        // 恢复 SpriteBatch 的默认 shader
        sb.setShader(null);
    }

    public void dispose() {
        if (shader != null) {
            shader.dispose();
        }
    }
}

