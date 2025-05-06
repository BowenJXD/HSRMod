package androidTestMod.relics.common;

import androidTestMod.AndroidTestMod;
import androidTestMod.utils.RelicEventHelper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public interface IRubertEmpireRelic {
    default void checkMerge() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean merge = p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalCogwheel.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalLever.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalPiston.ID));
        if (merge) {
            boolean b = true;
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof MergeEffect) {
                    b = false;
                    break;
                }
            }
            if (b) {
                AbstractDungeon.effectList.add(new MergeEffect());
            }
        }
    }
    
    class MergeEffect extends AbstractGameEffect {
        @Override
        public void update() {
            isDone = true;
            AbstractDungeon.player.loseRelic(AndroidTestMod.makePath(RubertEmpireMechanicalCogwheel.ID));
            AbstractDungeon.player.loseRelic(AndroidTestMod.makePath(RubertEmpireMechanicalLever.ID));
            AbstractDungeon.player.loseRelic(AndroidTestMod.makePath(RubertEmpireMechanicalPiston.ID));
            RelicEventHelper.gainRelics(AndroidTestMod.makePath(RubertEmpireDifferenceMachine.ID));
        }

        @Override
        public void render(SpriteBatch spriteBatch) {}

        @Override
        public void dispose() {}
    }
}
