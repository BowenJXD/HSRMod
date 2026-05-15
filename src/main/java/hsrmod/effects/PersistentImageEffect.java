package hsrmod.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PersistentImageEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;
    // 0 = no float, 1 or -1 = floating direction (matches BaseMonster.floatIndex convention)
    public float floatIndex = 0f;

    public PersistentImageEffect(String path, float x, float y) {
        Texture texture = ImageMaster.loadImage(path);
        if (texture == null) {
            isDone = true;
            return;
        }
        this.img = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        this.x = x;
        this.y = y;
        this.duration = Float.MAX_VALUE;
        this.color = Color.WHITE.cpy();
    }

    public PersistentImageEffect(String path, float x, float y, float floatIndex) {
        this(path, x, y);
        this.floatIndex = floatIndex;
    }

    @Override
    public void update() {
        // Persists indefinitely; call stop() to remove
    }

    @Override
    public void render(SpriteBatch sb) {
        if (img == null || isDone) return;
        float floatY = floatIndex != 0f
                ? floatIndex * MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale
                : 0f;
        sb.setColor(this.color);
        sb.setBlendFunction(770, 771);
        sb.draw(img,
                x - img.packedWidth / 2.0F,
                y - img.packedHeight / 2.0F + floatY,
                img.packedWidth / 2.0F, img.packedHeight / 2.0F,
                img.packedWidth, img.packedHeight,
                1f, 1f, 0f);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        isDone = true;
    }
}