package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PortraitDisplayEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;
    private float initialScale;
    private float targetScale;
    private float scaleX;
    private float scaleY;
    private float shrinkTime;  // Time to shrink to original size (A)
    private float stayTime;    // Time to stay on screen (B)
    private float fadeTime;    // Time to fade away (C)
    private float totalTime;   // Total time for the effect
    private boolean isShrinking = true;
    private boolean isFading = false;

    // Constructor
    public PortraitDisplayEffect(String charName) {
        Texture texture = ImageMaster.loadImage(String.format("HSRModResources/img/effects/portraits/%s.png", charName));  // Load the image
        if (texture == null) {
            isDone = true;
            return;
        }
        this.img = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());  // Load the image
        this.duration = shrinkTime + stayTime + fadeTime;  // Total duration
        this.shrinkTime = 0.1f;  // A
        this.stayTime = 0.3f;    // B
        this.fadeTime = 0.2f;    // C
        this.totalTime = 0f;

        // Calculate the target scale based on screen height
        this.initialScale = 1.5f;
        this.targetScale = 1;
        this.scaleX = 1;
        this.scaleY = 1;

        // Set initial position (centered on the screen)
        this.x = (Gdx.graphics.getWidth() - img.packedWidth * scaleX) / 2;
        this.y = (Gdx.graphics.getHeight() - img.packedHeight * scaleY) / 2;
        
        this.color = Color.WHITE;
    }

    @Override
    public void update() {
        totalTime += Gdx.graphics.getDeltaTime();

        if (totalTime < shrinkTime) {
            // Shrink image quickly to its original size
            float shrinkFactor = MathUtils.clamp(totalTime / shrinkTime, 0, 1);
            this.scaleX = this.scaleY = initialScale - (initialScale - targetScale) * shrinkFactor;
        } else if (totalTime < shrinkTime + stayTime) {
            // Keep the image at original size for the 'stayTime' duration
            this.scaleX = this.scaleY = targetScale;
        } else if (totalTime < shrinkTime + stayTime + fadeTime) {
            // Fade the image out
            float fadeFactor = MathUtils.clamp(1 - (totalTime - (shrinkTime + stayTime)) / fadeTime, 0, 1);
            this.color = new Color(1, 1, 1, fadeFactor);
        } else {
            // The effect is done
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color != null ? this.color : Color.WHITE);
        sb.setBlendFunction(770, 771);  // Set blending mode
        sb.draw(img, x, y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scaleX, scaleY, 0); // Draw the image with updated size
        sb.setBlendFunction(770, 771); // Reset blending mode
    }

    @Override
    public void dispose() {
        // Nothing to dispose
    }
}

