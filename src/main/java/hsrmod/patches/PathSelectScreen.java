package hsrmod.patches;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.ISubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import hsrmod.characters.StellaCharacter;

import java.util.ArrayList;

public class PathSelectScreen implements ISubscriber, CustomSavable<Integer> {
    private static final ArrayList<Path> PATHS = new ArrayList<>();

    public String curName = "";

    public String nextName = "";

    static {
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            PATHS.add(new Path("HSRModResources/img/char/TrailblazeLogo.png", "开拓命途", "开拓属于你的道路", hsrmod.modcore.Path.TRAILBLAZE));
            PATHS.add(new Path("HSRModResources/img/char/ElationLogo.png", "欢愉命途", "通过追击进行输出", hsrmod.modcore.Path.ELATION));
            PATHS.add(new Path("HSRModResources/img/char/DestructionLogo.png", "毁灭命途", "通过击破进行输出", hsrmod.modcore.Path.DESTRUCTION));
            PATHS.add(new Path("HSRModResources/img/char/NihilityLogo.png", "虚无命途", "通过持续伤害进行输出", hsrmod.modcore.Path.NIHILITY));
            PATHS.add(new Path("HSRModResources/img/char/PreservationLogo.png", "存护命途", "通过格挡进行输出", hsrmod.modcore.Path.PRESERVATION));
            PATHS.add(new Path("HSRModResources/img/char/TheHuntLogo.png", "巡猎命途", "通过摸牌进行输出", hsrmod.modcore.Path.THE_HUNT));
            PATHS.add(new Path("HSRModResources/img/char/PropagationLogo.png", "繁育命途", "通过能量进行输出", hsrmod.modcore.Path.PROPAGATION));
            PATHS.add(new Path("HSRModResources/img/char/EruditionLogo.png", "智识命途", "通过充能进行输出", hsrmod.modcore.Path.ERUDITION));
        }
        else {
            PATHS.add(new Path("HSRModResources/img/char/TrailblazeLogo.png", "Trailblaze", "Trailblaze your own way", hsrmod.modcore.Path.TRAILBLAZE));
            PATHS.add(new Path("HSRModResources/img/char/ElationLogo.png", "Elation", "Damage through Follow-Up", hsrmod.modcore.Path.ELATION));
            PATHS.add(new Path("HSRModResources/img/char/DestructionLogo.png", "Destruction", "Damage through break", hsrmod.modcore.Path.DESTRUCTION));
            PATHS.add(new Path("HSRModResources/img/char/NihilityLogo.png", "Nihility", "Damage through DoT", hsrmod.modcore.Path.NIHILITY));
            PATHS.add(new Path("HSRModResources/img/char/PreservationLogo.png", "Preservation", "Damage through block", hsrmod.modcore.Path.PRESERVATION));
            PATHS.add(new Path("HSRModResources/img/char/TheHuntLogo.png", "The Hunt", "Damage through draw", hsrmod.modcore.Path.THE_HUNT));
            PATHS.add(new Path("HSRModResources/img/char/PropagationLogo.png", "Propagation", "Damage through energy", hsrmod.modcore.Path.PROPAGATION));
            PATHS.add(new Path("HSRModResources/img/char/EruditionLogo.png", "Erudition", "Damage through charge", hsrmod.modcore.Path.ERUDITION));
        }
    }

    public static PathSelectScreen Inst = new PathSelectScreen();

    public Hitbox leftHb;

    public Hitbox rightHb;

    public TextureAtlas atlas;

    public Skeleton skeleton;

    public AnimationStateData stateData;

    public AnimationState state;

    public int index;

    public static Path getPath() {
        return PATHS.get(Inst.index);
    }

    public PathSelectScreen() {
        this.index = 0;
        refresh();
        this.leftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        this.rightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        BaseMod.subscribe(this);
    }

    public void refresh() {
        Path path = PATHS.get(this.index);
        this.curName = path.name;
        this.nextName = ((Path) PATHS.get(nextIndex())).name;
    }

    public int prevIndex() {
        return (this.index - 1 < 0) ? (PATHS.size() - 1) : (this.index - 1);
    }

    public int nextIndex() {
        return (this.index + 1 > PATHS.size() - 1) ? 0 : (this.index + 1);
    }

    public void update() {
        float centerX = Settings.WIDTH * 0.8F;
        float centerY = Settings.HEIGHT * 0.5F;
        this.leftHb.move(centerX - 200.0F * Settings.scale, centerY);
        this.rightHb.move(centerX + 200.0F * Settings.scale, centerY);
        updateInput();
    }

    private void updateInput() {
        if (CardCrawlGame.chosenCharacter == StellaCharacter.PlayerColorEnum.STELLA_CHARACTER) {
            this.leftHb.update();
            this.rightHb.update();
            if (this.leftHb.clicked) {
                this.leftHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = prevIndex();
                // CardCrawlGame.sound.play((getSkin()).word);
                refresh();
            }
            if (this.rightHb.clicked) {
                this.rightHb.clicked = false;
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.index = nextIndex();
                // CardCrawlGame.sound.play((getSkin()).word);
                refresh();
            }
            if (InputHelper.justClickedLeft) {
                if (this.leftHb.hovered)
                    this.leftHb.clickStarted = true;
                if (this.rightHb.hovered)
                    this.rightHb.clickStarted = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        Color RC = Color.valueOf("bacdbaff");
        float centerX = Settings.WIDTH * 0.8F;
        float centerY = Settings.HEIGHT * 0.5F;
        renderPath(sb, centerX, centerY);
        Color color = Settings.GOLD_COLOR.cpy();
        color.a /= 2.0F;
        float dist = 100.0F * Settings.scale;
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.curName, centerX, centerY, RC);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, (getPath()).effect, centerX, centerY - Settings.HEIGHT * 0.05F, RC);
        sb.draw((getPath()).t, centerX - Settings.WIDTH * 0.085F, centerY + Settings.HEIGHT * 0.035F);
        if (this.leftHb.hovered) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_LEFT_ARROW, this.leftHb.cX - 24.0F, this.leftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
        if (this.rightHb.hovered) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_RIGHT_ARROW, this.rightHb.cX - 24.0F, this.rightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
        this.rightHb.render(sb);
        this.leftHb.render(sb);
    }

    public void renderPath(SpriteBatch sb, float x, float y) {
        if (this.atlas == null)
            return;
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(x, y);
        sb.end();
        CardCrawlGame.psb.begin();
        AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
    }

    public static class Path {
        public String img;

        public Texture t;

        public String name;

        public String effect;

        public hsrmod.modcore.Path path;

        public Path(String img, String name, String effect, hsrmod.modcore.Path path) {
            this.img = img;
            this.t = ImageMaster.loadImage(img);
            this.name = name;
            this.effect = effect;
            this.path = path;
        }
    }

    public void onLoad(Integer arg0) {
        if (arg0 == null) return;
        this.index = arg0;
        refresh();
    }

    public Integer onSave() {
        return this.index;
    }
}

