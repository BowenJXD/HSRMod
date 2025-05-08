package hsrmod.effects;

import hsrmod.Hsrmod;
import hsrmod.relics.common.RubertEmpireDifferenceMachine;
import hsrmod.relics.common.RubertEmpireMechanicalCogwheel;
import hsrmod.relics.common.RubertEmpireMechanicalLever;
import hsrmod.relics.common.RubertEmpireMechanicalPiston;
import hsrmod.utils.RelicEventHelper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class MergeEffect extends AbstractGameEffect {
    @Override
    public void update() {
        isDone = true;
        AbstractDungeon.player.loseRelic(Hsrmod.makePath(RubertEmpireMechanicalCogwheel.ID));
        AbstractDungeon.player.loseRelic(Hsrmod.makePath(RubertEmpireMechanicalLever.ID));
        AbstractDungeon.player.loseRelic(Hsrmod.makePath(RubertEmpireMechanicalPiston.ID));
        RelicEventHelper.gainRelics(Hsrmod.makePath(RubertEmpireDifferenceMachine.ID));
    }

    @Override
    public void render(SpriteBatch spriteBatch) {}

    @Override
    public void dispose() {}
}
