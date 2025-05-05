uniform sampler2D u_texture;
uniform float intensity;
uniform float u_hueShift;  // 色相偏移

varying vec4 v_color;
varying vec2 v_texCoord;

// RGB 转 HSL
vec3 rgb2hsl(vec3 c) {
    float maxVal = max(c.r, max(c.g, c.b));
    float minVal = min(c.r, min(c.g, c.b));
    float h, s, l = (maxVal + minVal) / 2.0;
    if (maxVal == minVal) {
        h = s = 0.0; // 无色
    } else {
        float delta = maxVal - minVal;
        s = l > 0.5 ? delta / (2.0 - maxVal - minVal) : delta / (maxVal + minVal);
        if (maxVal == c.r) {
            h = (c.g - c.b) / delta + (c.g < c.b ? 6.0 : 0.0);
        } else if (maxVal == c.g) {
            h = (c.b - c.r) / delta + 2.0;
        } else {
            h = (c.r - c.g) / delta + 4.0;
        }
        h /= 6.0;
    }
    return vec3(h, s, l);
}

// HSL 转 RGB
vec3 hsl2rgb(vec3 hsl) {
    float r, g, b;
    if (hsl.y == 0.0) {
        r = g = b = hsl.z; // 灰度
    } else {
        float q = hsl.z < 0.5 ? hsl.z * (1.0 + hsl.y) : hsl.z + hsl.y - hsl.z * hsl.y;
        float p = 2.0 * hsl.z - q;
        r = hue2rgb(p, q, hsl.x + 1.0 / 3.0);
        g = hue2rgb(p, q, hsl.x);
        b = hue2rgb(p, q, hsl.x - 1.0 / 3.0);
    }
    return vec3(r, g, b);
}

// 色相调整
float hue2rgb(float p, float q, float t) {
    if (t < 0.0) t += 1.0;
    if (t > 1.0) t -= 1.0;
    if (t < 1.0 / 6.0) return p + (q - p) * 6.0 * t;
    if (t < 1.0 / 2.0) return q;
    if (t < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6.0;
    return p;
}

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    vec3 hsl = rgb2hsl(texColor.rgb);
    
    // 添加色相偏移（限制在 -15 到 +15 度之间）
    hsl.x += u_hueShift; 
    if (hsl.x > 1.0) hsl.x -= 1.0;
    if (hsl.x < 0.0) hsl.x += 1.0;

    // 应用灰度效果
    hsl.z = mix(hsl.z, 0.5, intensity); // 将亮度混合到灰度
    
    // 转换回 RGB
    vec3 resultColor = hsl2rgb(hsl);

    // 返回最终颜色
    gl_FragColor = vec4(resultColor, texColor.a);
}
