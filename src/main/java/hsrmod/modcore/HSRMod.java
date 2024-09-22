package hsrmod.modcore;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.badlogic.gdx.graphics.Color;
import hsrmod.characters.StellaCharacter;
import hsrmod.misc.ChargeIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.core.Settings.language;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.*;

@SpireInitializer // 加载mod的注解
public class HSRMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber {
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
    
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    
    String lang = "ENG";

    // 构造方法
    public HSRMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(HSR_PINK, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENERGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
        updateLanguage();
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new HSRMod();
    }

    @Override
    public void receiveEditCards() {
        CustomIconHelper.addCustomIcon(ChargeIcon.get());
        // This finds and adds all cards in the same package (or sub-package) as MyAbstractCard
        // along with marking all added cards as seen
        new AutoAdd(MOD_NAME)
                .packageFilter("hsrmod.cards")
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new StellaCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, STELLA_CHARACTER);
    }
    
    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_NAME)
                .packageFilter("hsrmod.relics")
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, HSR_PINK);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    public void receiveEditStrings() {
        updateLanguage();
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(CardStrings.class, "HSRModResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "HSRModResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "HSRModResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "HSRModResources/localization/" + lang + "/powers.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        updateLanguage();

        String json = Gdx.files.internal("HSRModResources/localization/" + lang + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword(MOD_NAME.toLowerCase(), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveAddAudio() {
        addAudio("Aventurine1");
        addAudio("Firefly1-1");
        addAudio("Firefly1-2");
        addAudio("Firefly2");
        addAudio("JingYuan1");
        addAudio("Kafka2");
        addAudio("Robin2");
        addAudio("Acheron1");
        addAudio("SlashedDream1");
        addAudio("SlashedDream2");
    }
    
    void addAudio(String id){
        BaseMod.addAudio(id, "HSRModResources/localization/" + lang + "/audio/" + id + ".wav");
    }
    
    public void updateLanguage()
    {
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }
        else {
            lang = "ENG";
        }
    }
    public static String makePath(String name){
        if (name.contains("Power")) {
            name = name.replace("Power", "");
        }
        else if (name.contains("Relic")) {
            name = name.replace("Relic", "");
        }
        return MOD_NAME + ":" + name;
    }
}