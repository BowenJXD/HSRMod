package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LinearSweepingBeamEffect extends AbstractGameEffect {
    // Fixed endpoint provided as input.
    private float fixedX;
    private float fixedY;
    // "Sweep center" for the moving (unfixed) end.
    private float sweepCenterX;
    private float sweepCenterY;
    // Current position of the free end.
    private float movingX;
    private float movingY;
    // Beam length (in game units)
    private float dst;
    // Determines the sweeping direction.
    private boolean isFlipped = false;

    private static final float DUR = 0.4F;
    private static final float SWEEP_WIDTH = 300.0F;
    private static TextureAtlas.AtlasRegion img;

    public LinearSweepingBeamEffect(float fixedX, float fixedY, float sweepCenterX, float sweepCenterY, boolean isFlipped) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }

        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.sweepCenterX = sweepCenterX;
        this.sweepCenterY = sweepCenterY;
        this.isFlipped = isFlipped;

        // Optionally, adjust fixed point offsets if needed.
        // For now we use the given fixedX, fixedY directly.

        this.color = Color.CYAN.cpy();
        this.duration = DUR;
        this.startingDuration = DUR;
        // Set scale to the global scale.
        this.scale = Settings.scale;
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        this.duration -= delta;

        // Calculate progress from 0 to 1.
        float progress = 1.0f - (this.duration / this.startingDuration);

        // If unflipped: free end moves from (tX - 200, tY) to (tX + 200, tY)
        // If flipped: reverse the direction.
        if (!isFlipped) {
            movingX = sweepCenterX - SWEEP_WIDTH * Settings.scale + SWEEP_WIDTH * 2 * Settings.scale * progress;
        } else {
            movingX = sweepCenterX + SWEEP_WIDTH * Settings.scale - SWEEP_WIDTH * 2 * Settings.scale * progress;
        }
        movingY = sweepCenterY;

        // Compute beam length from the fixed endpoint to the moving endpoint.
        this.dst = Vector2.dst(fixedX, fixedY, movingX, movingY) / Settings.scale;

        // Calculate rotation (angle) of the beam.
        // Use atan2(deltaX, deltaY) then convert to degrees.
        this.rotation = MathUtils.atan2(movingX - fixedX, movingY - fixedY) * (180f / MathUtils.PI);
        this.rotation = -this.rotation + 90.0f;

        // Fade the beam in and out.
        if (this.duration > this.startingDuration / 2.0F) {
            // First half: fade from opaque to transparent.
            this.color.a = Interpolation.pow2In.apply(1.0F, 0.0F, (this.duration - (this.startingDuration / 2.0F)) * 4.0F);
        } else {
            // Second half: fade back in.
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration * 4.0F);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        // Draw the beam from the fixed endpoint.
        // The image is drawn stretched from fixedX, fixedY along the beam.
        sb.draw(img, fixedX, fixedY - img.packedHeight / 2.0F,
                0.0F, img.packedHeight / 2.0F,
                this.dst, 50.0F,
                this.scale + MathUtils.random(-0.01F, 0.01F),
                this.scale, this.rotation);

        // Optionally draw a second pass with a tinted color.
        sb.setColor(new Color(0.3F, 0.3F, 1.0F, this.color.a));
        sb.draw(img, fixedX, fixedY - img.packedHeight / 2.0F,
                0.0F, img.packedHeight / 2.0F,
                this.dst, MathUtils.random(50.0F, 90.0F),
                this.scale + MathUtils.random(-0.02F, 0.02F),
                this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
        // No additional resources to dispose.
    }
}

