package androidTestMod.characters;

import androidTestMod.AndroidTestMod;
import androidTestMod.cards.base.*;
import androidTestMod.relics.starter.*;
import androidTestMod.utils.PathSelectScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;

// 继承CustomPlayer类
public class StellaCharacter extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = "img/char/shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = "img/char/shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = "img/char/corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            "img/UI/orb/layer1.png",
            "img/UI/orb/layer2.png",
            "img/UI/orb/layer3.png",
            "img/UI/orb/layer4.png",
            "img/UI/orb/layer5.png",
            "img/UI/orb/layer6.png",
            "img/UI/orb/layer1d.png",
            "img/UI/orb/layer2d.png",
            "img/UI/orb/layer3d.png",
            "img/UI/orb/layer4d.png",
            "img/UI/orb/layer5d.png",
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(AndroidTestMod.makePath("StellaCharacter"));

    float drawYAlter = 0;
    
    public StellaCharacter(String name) {
        super(AndroidTestMod.MOD_NAME, name, STELLA_CHARACTER, ORB_TEXTURES, "img/UI/orb/vfx.png", LAYER_SPEED, null, null);

        String charImg = null;
        float hbx = 0f, hby = 0f, hbw = 200f, hbh = 220f;

        // 如果你的人物没有动画，那么这些不需要写
            try {
                this.loadAnimation("img/spine/nv.atlas", "img/spine/nv.json", 3F);
                AnimationState.TrackEntry e = this.state.setAnimation(0, "idle_back", true);
                e.setTime(e.getEndTime() * MathUtils.random());
                e.setTimeScale(0.5F);
                float ratio = (float) Settings.WIDTH / Settings.HEIGHT;
                float dy = 0;
                hbx = 50f;
                hbw = 200f;
                if (ratio > 2.3f /* 2.3+ */) {
                    hby = -380f;
                    hbh = 300f;
                    dy = 50f;
                } else if (ratio > 2f /* 2.3 - 2 */) {
                    hby = -320f;
                    hbh = 300f;
                } else if (ratio > 1.9f /* 2 - 1.9 */) {
                    hby = -380f;
                    hbh = 300f;
                } else if (ratio > 1.5f /* 1.9 - 1.77 - 1.6 - 1.5 */) {
                    hby = -280f;
                    hbh = 300f;
                } else if (ratio > 1.3f /* 1.5 - 1.33 - 1.3 */) {
                    if ((float) Settings.M_W / Settings.M_H > 1.3f) {
                        hby = -380f;
                        hbh = 400f;
                    } else {
                        hby = -280f;
                        hbh = 300f;
                    }
                } else /* 1.3- */ {
                    hby = -280f;
                    hbh = 300f;
                }
                drawYAlter = hbh * Settings.scale * 0.9f + dy;
                this.drawY = AbstractDungeon.floorY + drawYAlter;
            } catch (Exception e) {
                AndroidTestMod.logger.error("Failed to load animation: {}", e.getMessage());
                charImg = "img/char/character.png";
            }
        /*else {
            charImg = "img/char/character.png";
        }*/
        
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                charImg, // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                hbx, hby,
                hbw, hbh, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );
        
        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 70.0F * Settings.scale);
        // this.dialogY = (this.drawY + 0.0F * Settings.scale);
    }

    protected void initializeClass(String imgUrl, String shoulder2ImgUrl, String shouldImgUrl, String corpseImgUrl, CharSelectInfo info, float hb_x, float hb_y, float hb_w, float hb_h, EnergyManager energy) {
        if (imgUrl != null) {
            this.img = AssetLoader.getTexture(AndroidTestMod.MOD_NAME,imgUrl);
        }

        if (this.img != null) {
            this.atlas = null;
        }

        this.shoulderImg = AssetLoader.getTexture(AndroidTestMod.MOD_NAME,shouldImgUrl);
        this.shoulder2Img = AssetLoader.getTexture(AndroidTestMod.MOD_NAME,shoulder2ImgUrl);
        this.corpseImg = AssetLoader.getTexture(AndroidTestMod.MOD_NAME,corpseImgUrl);


        if (Settings.isMobile) {
            hb_w *= 1.17F;
        }

        this.maxHealth = info.maxHp;
        this.startingMaxHP = this.maxHealth;
        this.currentHealth = info.currentHp;
        this.masterMaxOrbs = info.maxOrbs;
        this.energy = energy;
        this.masterHandSize = info.cardDraw;
        this.gameHandSize = this.masterHandSize;
        this.gold = info.gold;
        this.displayGold = this.gold;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_w = hb_w * Settings.scale;
        this.hb_x = hb_x * Settings.scale;
        this.hb_y = hb_y * Settings.scale;
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);
        this.refreshHitboxLocation();
    }

    @Override
    public void update() {
        super.update();
        if (state != null) {
            flipHorizontal = true;
        }
    }

    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        try {
            super.renderPlayerImage(sb);
        } catch (Exception e) {
            AndroidTestMod.logger.error("Failed to render player image: {}", e.getMessage());
        }
    }

    @Override
    public void playDeathAnimation() {
        super.playDeathAnimation();
        drawY = AbstractDungeon.floorY;
    }

    @Override
    public void movePosition(float x, float y) {
        if (y == AbstractDungeon.floorY && state != null) {
            y += drawYAlter;
        }
        super.movePosition(x, y);
    }

    // 初始卡组的ID，可直接写或引用变量
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(AndroidTestMod.makePath(Trailblazer1.ID));
        retVal.add(AndroidTestMod.makePath(March7th0.ID));
        retVal.add(AndroidTestMod.makePath(Danheng0.ID));
        retVal.add(AndroidTestMod.makePath(Himeko0.ID));
        retVal.add(AndroidTestMod.makePath(Welt0.ID));
        retVal.add(AndroidTestMod.makePath(Trailblazer2.ID));
        retVal.add(AndroidTestMod.makePath(Trailblazer3.ID));
        retVal.add(AndroidTestMod.makePath(Trailblazer3.ID));
        retVal.add(AndroidTestMod.makePath(Trailblazer3.ID));
        retVal.add(AndroidTestMod.makePath(Trailblazer3.ID));

        return retVal;
    }

    // 初始遗物的ID，可以先写个原版遗物凑数
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(GalacticBat.ID);
        retVal.add(PomPomBlessing.ID);
        switch (PathSelectScreen.getPath().path) {
            case ELATION:
                retVal.add(AndroidTestMod.makePath(WaxOfElation.ID));
                break;
            case DESTRUCTION:
                retVal.add(AndroidTestMod.makePath(WaxOfDestruction.ID));
                break;
            case NIHILITY:
                retVal.add(AndroidTestMod.makePath(WaxOfNihility.ID));
                break;
            case PROPAGATION:
                retVal.add(AndroidTestMod.makePath(WaxOfPropagation.ID));
                break;
            case PRESERVATION:
                retVal.add(AndroidTestMod.makePath(WaxOfPreservation.ID));
                break;
            case THE_HUNT:
                retVal.add(AndroidTestMod.makePath(WaxOfTheHunt.ID));
                break;
            case ERUDITION:
                retVal.add(AndroidTestMod.makePath(WaxOfErudition.ID));
                break;
            case ABUNDANCE:
                retVal.add(AndroidTestMod.makePath(WaxOfAbundance.ID));
                break;
            /*case REMEMBRANCE:
                retVal.add(HSRMod.makePath(WaxOfRemembrance.ID));
                break;*/
            case TRAILBLAZE:
            default:
                retVal.add(AndroidTestMod.makePath(TrailblazeTimer.ID));
                break;
        }
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                73, // 当前血量
                73, // 最大血量
                0, // 初始充能球栏位
                73, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return HSR_PINK;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Trailblazer1();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return AndroidTestMod.MY_COLOR;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 6;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.play("Stelle1");
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel("img/char/Victory1.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("img/char/Victory2.png"));
        panels.add(new CutscenePanel("img/char/Victory3.png"));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "Stelle1";
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new StellaCharacter(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return AndroidTestMod.MY_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return AndroidTestMod.MY_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    // 以下为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用

    // 注意此处是在 MyCharacter 类内部的静态嵌套类中定义的新枚举值
    // 不可将该定义放在外部的 MyCharacter 类中，具体原因见《高级技巧 / 01 - Patch / SpireEnum》
    public static class PlayerColorEnum {
        public static PlayerClass STELLA_CHARACTER;

        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***
        public static AbstractCard.CardColor HSR_PINK;
    }

    public static class PlayerLibraryEnum {
        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***

        // 这个变量未被使用（呈现灰色）是正常的
        public static CardLibrary.LibraryType HSR_PINK;
    }
}