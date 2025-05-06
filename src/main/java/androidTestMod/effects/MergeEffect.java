package androidTestMod.effects;

import androidTestMod.AndroidTestMod;
import androidTestMod.relics.common.RubertEmpireDifferenceMachine;
import androidTestMod.relics.common.RubertEmpireMechanicalCogwheel;
import androidTestMod.relics.common.RubertEmpireMechanicalLever;
import androidTestMod.relics.common.RubertEmpireMechanicalPiston;
import androidTestMod.utils.RelicEventHelper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class MergeEffect extends AbstractGameEffect {
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
