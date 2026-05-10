package hsrmod.dungeons;

import actlikeit.dungeons.CustomDungeon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.BottomFogEffect;
import com.megacrit.cardcrawl.vfx.scene.ShinySparkleEffect;
import com.megacrit.cardcrawl.vfx.scene.WobblyCircleEffect;
import hsrmod.monsters.Exordium.*;
import hsrmod.monsters.TheEnding.AntiCreator;
import hsrmod.monsters.TheEnding.Zandar;

import java.util.ArrayList;
import java.util.Iterator;

public class AmphoreusScene extends AbstractScene {
    private ArrayList<AbstractGameEffect> circles = new ArrayList();
    
    private TextureAtlas.AtlasRegion customBg; //
    private TextureAtlas.AtlasRegion campfireBg; //
    protected TextureAtlas customAtlas; //

    public AmphoreusScene() {
        super("endingScene/scene.atlas");
        this.ambianceName = "AMBIANCE_BEYOND";
        this.fadeInAmbiance();
        
        customAtlas = new TextureAtlas(Gdx.files.internal("HSRModResources/img/scene/atlas.atlas"));
        campfireBg = customAtlas.findRegion("mod/AmphoreusCampfire");
    }

    public void update() {
        super.update();
        this.updateParticles();
    }

    public void randomizeScene() {
    }

    private void updateParticles() {
        Iterator<AbstractGameEffect> e = this.circles.iterator();

        while(e.hasNext()) {
            AbstractGameEffect effect = (AbstractGameEffect)e.next();
            effect.update();
            if (effect.isDone) {
                e.remove();
            }
        }

        if (!(AbstractDungeon.getCurrRoom() instanceof TrueVictoryRoom) && this.circles.size() < 72 && !Settings.DISABLE_EFFECTS) {
            if (MathUtils.randomBoolean(0.2F)) {
                this.circles.add(new ShinySparkleEffect());
            } else {
                this.circles.add(new WobblyCircleEffect());
            }
        }

    }

    public void nextRoom(AbstractRoom room) {
        super.nextRoom(room);
        this.randomizeScene();
        /*if (room instanceof MonsterRoomBoss) {
            CardCrawlGame.music.silenceBGM();
        }*/

        //
        this.customBg = null;

        if (room.monsters != null) {
            ArrayList<AbstractMonster> monsters = room.monsters.monsters;

            if (monsters.stream().anyMatch(m -> m instanceof Zandar)) {
                this.customBg = customAtlas.findRegion("mod/AmphoreusZandar");
            } else if (monsters.stream().anyMatch(m -> m instanceof AntiCreator)) {
                this.customBg = customAtlas.findRegion("mod/AmphoreusIrontomb");
            } else if (room instanceof RestRoom) {
                room.playBGM("Maze");
            } else {
                room.playBGM("Homeland");
            }
        } else {
            CustomDungeon.resumeMainMusic();
        }
        //
        this.fadeInAmbiance();
    }

    public void renderCombatRoomBg(SpriteBatch sb) {
        if (customBg != null) {
            sb.setColor(Color.WHITE.cpy());
            this.renderAtlasRegionIf(sb, this.customBg, true);
            GL20 var10001 = Gdx.gl20;
            GL20 var10002 = Gdx.gl20;
            sb.setBlendFunction(770, 771);
            return;
        }
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.bg, true);
    }

    public void renderCombatRoomFg(SpriteBatch sb) {
        if (!this.isCamp) {
            sb.setBlendFunction(770, 1);

            for(AbstractGameEffect e : this.circles) {
                e.render(sb);
            }

            sb.setBlendFunction(770, 771);
        }

    }

    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireBg, true);
    }
}
