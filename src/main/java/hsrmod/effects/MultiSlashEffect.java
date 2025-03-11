package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class MultiSlashEffect extends AbstractGameEffect {
    private float x;
    private float y;
    int count;
    Color color;
    Color color2;
    float angle;
    float interval = 0.25f;

    public MultiSlashEffect(float x, float y, int count, Color color, Color color2) {
        this.x = x;
        this.y = y;
        this.count = count;
        this.color = color;
        this.color2 = color2;
        this.interval = Settings.FAST_MODE ? 0.12f : 0.25f;
        this.startingDuration = interval * count;
        this.duration = this.startingDuration;
        this.angle = 0;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (duration <= interval * count) {
            count--;
            if (count == 0) return;
            angle += MathUtils.random(120, 240);
            float size = count == -1 ? 6F : MathUtils.random(3L, 4L);
            float dx = -MathUtils.cosDeg(angle - 90) * size * 100;
            float dy = -MathUtils.sinDeg(angle - 90) * size * 100;
            CardCrawlGame.sound.playA(count == -1 ? "ATTACK_HEAVY" : "ATTACK_FAST", -0.4F);

            AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x, this.y - 30.0F * Settings.scale, dx, dy, angle, size, color, color2));
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void dispose() {

    }
}
