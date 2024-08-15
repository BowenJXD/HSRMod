package hsrmod.modcore;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import hsrmod.cards.*;
import com.badlogic.gdx.graphics.Color;
import hsrmod.characters.MyCharacter;
import hsrmod.relics.GalacticBat;
import hsrmod.relics.GoldenTrash;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.*;

@SpireInitializer // 加载mod的注解
public class HSRMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber {
    public static final String MOD_NAME = "HSRMod";
    
    public static final Color MY_COLOR = new Color(255.0F / 255.0F, 141.0F / 255.0F, 227.0F / 255.0F, 1.0F);

    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "HSRModResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "HSRModResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "HSRModResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "HSRModResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "HSRModResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "HSRModResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "HSRModResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "HSRModResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "HSRModResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "HSRModResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "HSRModResources/img/char/cost_orb.png";

    // 构造方法
    public HSRMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(HSR_PINK, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENERGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new HSRMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Trailblazer1());
        BaseMod.addCard(new Trailblazer2());
        BaseMod.addCard(new Trailblazer3());
        BaseMod.addCard(new Sushang1());
        BaseMod.addCard(new Tingyun1());
        BaseMod.addCard(new BlackSwan2());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }
    
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new GoldenTrash(), RelicType.SHARED);
        BaseMod.addRelic(new GalacticBat(), RelicType.SHARED);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "HSRModResources/localization/" + lang + "/characters.json");

        BaseMod.loadCustomStringsFile(RelicStrings.class, "HSRModResources/localization/" + lang + "/relics.json");

        BaseMod.loadCustomStringsFile(PowerStrings.class, "HSRModResources/localization/" + lang + "/powers.json");
    }

    public static String makePath(String name){
        if (name.contains("Power")) {
            name = name.replace("Power", "");
        }
        return MOD_NAME + ":" + name;
    }
}