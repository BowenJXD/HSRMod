package hsrmod.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import hsrmod.cards.base.*;
import hsrmod.misc.IHSRCharacter;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.patches.PathSelectScreen;
import hsrmod.relics.starter.*;
import hsrmod.utils.PathDefine;

import java.util.ArrayList;

import static hsrmod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;

// 继承CustomPlayer类
public class StellaCharacter extends CustomPlayer implements IHSRCharacter {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = PathDefine.CHARACTER_PATH + "shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = PathDefine.CHARACTER_PATH + "shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = PathDefine.CHARACTER_PATH + "corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            PathDefine.UI_PATH + "orb/layer1.png",
            PathDefine.UI_PATH + "orb/layer2.png",
            PathDefine.UI_PATH + "orb/layer3.png",
            PathDefine.UI_PATH + "orb/layer4.png",
            PathDefine.UI_PATH + "orb/layer5.png",
            PathDefine.UI_PATH + "orb/layer6.png",
            PathDefine.UI_PATH + "orb/layer1d.png",
            PathDefine.UI_PATH + "orb/layer2d.png",
            PathDefine.UI_PATH + "orb/layer3d.png",
            PathDefine.UI_PATH + "orb/layer4d.png",
            PathDefine.UI_PATH + "orb/layer5d.png",
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("HSRMod:StellaCharacter");

    float drawYAlter = 0;
    
    public StellaCharacter(String name) {
        super(name, STELLA_CHARACTER, ORB_TEXTURES,PathDefine.UI_PATH + "orb/vfx.png", LAYER_SPEED, null, null);

        String charImg = null;
        float hbx = 0f, hby = 0f, hbw = 200f, hbh = 220f;

        // 如果你的人物没有动画，那么这些不需要写
        if (HSRModConfig.useSpine) {
            try {
                this.loadAnimation("HSRModResources/img/spine/nv.atlas", "HSRModResources/img/spine/nv.json", 3F);
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
                HSRMod.logger.error("Failed to load animation: {}", e.getMessage());
                charImg = PathDefine.CHARACTER_PATH + "character.png";
            }
        } else {
            charImg = PathDefine.CHARACTER_PATH + "character.png";
        }
        
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

    @Override
    public void update() {
        super.update();
        if (state != null && !flipHorizontal) {
            if (AbstractDungeon.currMapNode == null 
                    || AbstractDungeon.currMapNode.room == null 
                    || AbstractDungeon.currMapNode.room.monsters == null 
                    || AbstractDungeon.currMapNode.room.monsters.monsters == null 
                    || AbstractDungeon.currMapNode.room.monsters.monsters.stream().noneMatch(m -> m.hasPower(BackAttackPower.POWER_ID)))
                flipHorizontal = true;
        }
    }

    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        try {
            if (BaseMod.hasModID("spireTogether:") && CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT) {
                drawY = Settings.HEIGHT * 0.5f + hb_h/2f;
            }
            super.renderPlayerImage(sb);
        } catch (Exception e) {
            HSRMod.logger.error("Failed to render player image: {}", e.getMessage());
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

        retVal.add(HSRMod.makePath(Trailblazer1.ID));
        retVal.add(HSRMod.makePath(March7th0.ID));
        retVal.add(HSRMod.makePath(Danheng0.ID));
        retVal.add(HSRMod.makePath(Himeko0.ID));
        retVal.add(HSRMod.makePath(Welt0.ID));
        retVal.add(HSRMod.makePath(Trailblazer2.ID));
        retVal.add(HSRMod.makePath(Trailblazer3.ID));
        retVal.add(HSRMod.makePath(Trailblazer3.ID));
        retVal.add(HSRMod.makePath(Trailblazer3.ID));
        retVal.add(HSRMod.makePath(Trailblazer3.ID));

        return retVal;
    }

    // 初始遗物的ID，可以先写个原版遗物凑数
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(GalacticBat.ID);
        retVal.add(PomPomBlessing.ID);
        switch (PathSelectScreen.getPath().path) {
            case ELATION:
                retVal.add(HSRMod.makePath(WaxOfElation.ID));
                break;
            case DESTRUCTION:
                retVal.add(HSRMod.makePath(WaxOfDestruction.ID));
                break;
            case NIHILITY:
                retVal.add(HSRMod.makePath(WaxOfNihility.ID));
                break;
            case PROPAGATION:
                retVal.add(HSRMod.makePath(WaxOfPropagation.ID));
                break;
            case PRESERVATION:
                retVal.add(HSRMod.makePath(WaxOfPreservation.ID));
                break;
            case THE_HUNT:
                retVal.add(HSRMod.makePath(WaxOfTheHunt.ID));
                break;
            case ERUDITION:
                retVal.add(HSRMod.makePath(WaxOfErudition.ID));
                break;
            case ABUNDANCE:
                retVal.add(HSRMod.makePath(WaxOfAbundance.ID));
                break;
            /*case REMEMBRANCE:
                retVal.add(HSRMod.makePath(WaxOfRemembrance.ID));
                break;*/
            case TRAILBLAZE:
            default:
                retVal.add(HSRMod.makePath(TrailblazeTimer.ID));
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
        return HSRMod.MY_COLOR;
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
        panels.add(new CutscenePanel(PathDefine.CHARACTER_PATH + "Victory1.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel(PathDefine.CHARACTER_PATH + "Victory2.png"));
        panels.add(new CutscenePanel(PathDefine.CHARACTER_PATH + "Victory3.png"));
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
        return HSRMod.MY_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return HSRMod.MY_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        if (AbstractDungeon.floorNum > 1 && HSRModConfig.firstTime) {
            HSRModConfig.setFirstTime(false);
        }
        // HSRModConfig.setFirstTime(true);
    }

    // 以下为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用

    // 注意此处是在 MyCharacter 类内部的静态嵌套类中定义的新枚举值
    // 不可将该定义放在外部的 MyCharacter 类中，具体原因见《高级技巧 / 01 - Patch / SpireEnum》
    public static class PlayerColorEnum {
        @SpireEnum
        public static PlayerClass STELLA_CHARACTER;

        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***
        @SpireEnum
        public static AbstractCard.CardColor HSR_PINK;
    }

    public static class PlayerLibraryEnum {
        // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
        // ***并且名称需要一致！***

        // 这个变量未被使用（呈现灰色）是正常的
        @SpireEnum
        public static CardLibrary.LibraryType HSR_PINK;
    }
}