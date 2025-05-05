package androidTestMod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.spine.BoneData;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BackFlipEffect extends AbstractGameEffect {
    private BoneData root;
    private AbstractPlayer player;
    private boolean back;

    public BackFlipEffect(AbstractPlayer player, boolean back) {
        this.player = player;
        this.back = back;
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
        if (player.state != null) {
            this.root = player.state.getData().getSkeletonData().findBone("root");
        }

    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
            if (this.root != null) {
                this.root.setRotation(0.0F);
            }

        } else {
            if (this.player.state != null) {
                this.root = this.player.state.getData().getSkeletonData().findBone("root");
            }

            if (this.root != null) {
                if (this.back) {
                    this.root.setRotation(Interpolation.exp10.apply(0.0F, 360.0F, 1.0F - this.duration / this.startingDuration));
                } else {
                    this.root.setRotation(Interpolation.exp10.apply(0.0F, 360.0F, this.duration / this.startingDuration));
                }
            }

        }
    }

    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}
