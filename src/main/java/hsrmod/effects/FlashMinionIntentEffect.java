package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.minion.AbstractPlayerMinion;

public class FlashMinionIntentEffect extends AbstractGameEffect {
    private static final float DURATION = 1.0F;
    private static final float FLASH_INTERVAL = 0.17F;
    private float intervalTimer = 0.0F;
    private final Texture img;
    private final AbstractPlayerMinion m;

    public FlashMinionIntentEffect(Texture img, AbstractPlayerMinion m) {
        this.duration = 1.0F;
        this.img = img;
        this.m = m;
    }

    public void update() {
        this.intervalTimer -= Gdx.graphics.getDeltaTime();
        if (this.intervalTimer < 0.0F && !this.m.isDying) {
            this.intervalTimer = 0.17F;
            AbstractDungeon.effectsQueue.add(new FlashMinionIntentParticle(this.img, this.m));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
