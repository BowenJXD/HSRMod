package androidTestMod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class MultiShivFreezeEffect extends AbstractGameEffect {private float interval = 0.0F;
    // 用于记录所有生成过的刀光参数
    private ArrayList<ThrowShivFreezeEffect.ShivParams> shivParamsList = new ArrayList<>();
    // 标记是否已经重构过刀光
    private boolean rebuilt = false;
    Color[] colors;

    public MultiShivFreezeEffect(Color... colors) {
        this.duration = 1F;
        this.colors = colors;
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        this.interval -= delta;

        // 只有当剩余时间大于 0.2 秒时才生成新的刀光
        if (this.duration > 0.5F) {
            if (this.interval < 0.0F) {
                // 每隔 0.02 ~ 0.05 秒生成一定数量的刀光
                this.interval = MathUtils.random(0.05F, 0.10F);
                int count = MathUtils.random(1, 4);
                for (int i = 0; i < count; ++i) {
                    float baseX = MathUtils.random(1200.0F, 2000.0F) * Settings.scale;
                    float baseY = AbstractDungeon.floorY + MathUtils.random(-100.0F, 500.0F) * Settings.scale;
                    ThrowShivFreezeEffect effect = new ThrowShivFreezeEffect(baseX, baseY, colors[i % colors.length], i % 2 == 0);
                    AbstractDungeon.effectsQueue.add(effect);
                    // 记录该效果的参数，用于后续重构
                    shivParamsList.add(effect.getShivParams());
                }
            }
        }

        this.duration -= delta;
        if (this.duration < 0.0F) {
            for (ThrowShivFreezeEffect.ShivParams params : shivParamsList) {
                // 使用第二个构造器，仅播放后半段动画（0.2秒）
                CardCrawlGame.sound.playA("BLUNT_FAST", -0.2F);
                ThrowShivFreezeEffect rebuiltEffect = new ThrowShivFreezeEffect(params);
                AbstractDungeon.effectsQueue.add(rebuiltEffect);
            }
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        // 本效果自身不直接渲染内容
    }

    public void dispose() {
    }
}
