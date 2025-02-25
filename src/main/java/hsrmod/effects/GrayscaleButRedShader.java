package hsrmod.effects;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GrayscaleButRedShader {
    private static final String VERT = "" +
            "uniform mat4 u_projTrans;\n" +
            "\n" +
            "attribute vec4 a_position;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "attribute vec4 a_color;\n" +
            "\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoord;\n" +
            "\n" +
            "uniform vec2 u_viewportInverse;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = u_projTrans * a_position;\n" +
            "    v_texCoord = a_texCoord0;\n" +
            "    v_color = a_color;\n" +
            "}";
    private static final String FRAG = "" +
            "uniform sampler2D u_texture;\n" +
            "uniform float intensity;  // 0 -> 原色, 1 -> 完全灰度\n" +
            "uniform float hueThreshold; // 色相阈值\n" +
            "\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoord;\n" +
            "\n" +
            "// RGB 转 HSL\n" +
            "vec3 rgbToHsl(vec3 color) {\n" +
            "    float cmax = max(color.r, max(color.g, color.b));\n" +
            "    float cmin = min(color.r, min(color.g, color.b));\n" +
            "    float delta = cmax - cmin;\n" +
            "\n" +
            "    float h = 0.0;\n" +
            "    if (delta == 0.0) {\n" +
            "        h = 0.0;\n" +
            "    } else if (cmax == color.r) {\n" +
            "        h = mod((color.g - color.b) / delta, 6.0);\n" +
            "    } else if (cmax == color.g) {\n" +
            "        h = (color.b - color.r) / delta + 2.0;\n" +
            "    } else {\n" +
            "        h = (color.r - color.g) / delta + 4.0;\n" +
            "    }\n" +
            "\n" +
            "    h = h * 60.0;\n" +
            "    if (h < 0.0) h += 360.0;\n" +
            "\n" +
            "    float l = (cmax + cmin) / 2.0;\n" +
            "    float s = (delta == 0.0) ? 0.0 : (delta / (1.0 - abs(2.0 * l - 1.0)));\n" +
            "\n" +
            "    return vec3(h, s, l);\n" +
            "}\n" +
            "\n" +
            "// HSL 转 RGB\n" +
            "vec3 hslToRgb(vec3 hsl) {\n" +
            "    float c = (1.0 - abs(2.0 * hsl.z - 1.0)) * hsl.y;\n" +
            "    float x = c * (1.0 - abs(mod(hsl.x / 60.0, 2.0) - 1.0));\n" +
            "    float m = hsl.z - c / 2.0;\n" +
            "\n" +
            "    vec3 rgb = vec3(0.0, 0.0, 0.0);\n" +
            "    if (hsl.x >= 0.0 && hsl.x < 60.0) {\n" +
            "        rgb = vec3(c, x, 0.0);\n" +
            "    } else if (hsl.x >= 60.0 && hsl.x < 120.0) {\n" +
            "        rgb = vec3(x, c, 0.0);\n" +
            "    } else if (hsl.x >= 120.0 && hsl.x < 180.0) {\n" +
            "        rgb = vec3(0.0, c, x);\n" +
            "    } else if (hsl.x >= 180.0 && hsl.x < 240.0) {\n" +
            "        rgb = vec3(0.0, x, c);\n" +
            "    } else if (hsl.x >= 240.0 && hsl.x < 300.0) {\n" +
            "        rgb = vec3(x, 0.0, c);\n" +
            "    } else {\n" +
            "        rgb = vec3(c, 0.0, x);\n" +
            "    }\n" +
            "\n" +
            "    return rgb + vec3(m, m, m);\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 texColor = texture2D(u_texture, v_texCoord);\n" +
            "    \n" +
            "    // 将颜色转换为 HSL\n" +
            "    vec3 hsl = rgbToHsl(texColor.rgb);\n" +
            "\n" +
            "    float diff = abs(mod(hsl.x - 180.0, 360.0) - 180.0); // 计算色相差值\n" +
            "    if (diff < hueThreshold) {\n" +
            "        // 如果色相在范围内，保持原始颜色\n" +
            "        gl_FragColor = texColor * v_color;\n" +
            "    } else {\n" +
            "        // 否则应用灰度处理\n" +
            "        float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
            "        texColor.rgb = mix(texColor.rgb, vec3(gray), intensity);\n" +
            "        gl_FragColor = texColor * v_color;\n" +
            "    }\n" +
            "}";
    public static ShaderProgram program = new ShaderProgram(VERT, FRAG);

    public GrayscaleButRedShader() {
    }
}
