package hsrmod.relics.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.RelicEventHelper;

public interface IRubertEmpireRelic {
    default void checkMerge() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean merge = p.hasRelic(HSRMod.makePath(RubertEmpireMechanicalCogwheel.ID))
                && p.hasRelic(HSRMod.makePath(RubertEmpireMechanicalLever.ID))
                && p.hasRelic(HSRMod.makePath(RubertEmpireMechanicalPiston.ID));
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
            AbstractDungeon.player.loseRelic(HSRMod.makePath(RubertEmpireMechanicalCogwheel.ID));
            AbstractDungeon.player.loseRelic(HSRMod.makePath(RubertEmpireMechanicalLever.ID));
            AbstractDungeon.player.loseRelic(HSRMod.makePath(RubertEmpireMechanicalPiston.ID));
            RelicEventHelper.gainRelics(HSRMod.makePath(RubertEmpireDifferenceMachine.ID));
        }

        @Override
        public void render(SpriteBatch spriteBatch) {}

        @Override
        public void dispose() {}
    }
}
