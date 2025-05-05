package androidTestMod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FloatingImageEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private TextureAtlas.AtlasRegion img;
    private Color startColor;
    private Color endColor;
    private float startX, startY;
    private Vector2 direction;
    private float rotationSpeed; // The speed of rotation
    private float currentRotation; // The current rotation angle

    public FloatingImageEffect(Hitbox hb, TextureAtlas.AtlasRegion[] images) {
        // Randomly select an image
        this.img = images[MathUtils.random(images.length - 1)];
        float startAlpha = MathUtils.random(0.4F, 0.8F); // Random start alpha
        this.startColor = new Color(1, 1, 1, startAlpha); // Start with 80% opacity
        this.endColor = new Color(1, 1, 1, 0); // Fade to transparent

        this.duration = 3.0F; // The effect will last for 3 seconds

        // Set scale based on the size of the hitbox
        this.scale = MathUtils.random(1.0F, 1.5F) * Settings.scale;
        this.x = hb.cX - img.packedWidth / 2.0F * scale;
        this.y = hb.cY - img.packedHeight / 2.0F * scale;

        // Random direction (angle)
        float angle = MathUtils.random(360.0F);
        this.direction = new Vector2(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));

        // Initial speed (how fast it will move)
        float speed = MathUtils.random(30.0F, 60.0F) * Settings.scale;
        this.vX = direction.x * speed;
        this.vY = direction.y * speed;

        // Rotation speed (degrees per second)
        this.rotationSpeed = MathUtils.random(15.0F, 30.0F); // Slow rotation speed
        if (MathUtils.randomBoolean()) {
            this.rotationSpeed *= -1; // Randomly reverse the rotation direction
        }
        this.currentRotation = 0; // Initial rotation angle
    }

    @Override
    public void update() {
        // Move the image based on velocity
        x += vX * Gdx.graphics.getDeltaTime();
        y += vY * Gdx.graphics.getDeltaTime();

        // Apply rotation
        currentRotation += rotationSpeed * Gdx.graphics.getDeltaTime();
        if (currentRotation >= 360) {
            currentRotation -= 360; // Keep the rotation within 0-360 degrees
        }

        // Fade the effect
        float alpha = Interpolation.fade.apply(startColor.a, endColor.a, 1.0F - this.duration / 3.0F);
        this.color = new Color(startColor.r, startColor.g, startColor.b, alpha);

        // Decrease the duration
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale, scale, currentRotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
