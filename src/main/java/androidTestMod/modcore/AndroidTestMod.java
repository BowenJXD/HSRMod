package androidTestMod.modcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomRelic;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.android.mods.interfaces.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import androidTestMod.characters.StellaCharacter;
import androidTestMod.misc.ToughnessReductionVariable;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.PathSelectManager;
import androidTestMod.utils.RewardEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import static com.megacrit.cardcrawl.core.Settings.language;
import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;

public final class AndroidTestMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, StartGameSubscriber {
    public static String MOD_NAME = "AndroidTestMod";

    public static final Color MY_COLOR = new Color(255.0F / 255.0F, 141.0F / 255.0F, 227.0F / 255.0F, 1.0F);

    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "img/char/cost_orb.png";

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    String lang = "ENG";

    // 构造方法
    public AndroidTestMod() {
        BaseMod.subscribe(this);
        updateLanguage();

        CardColorBundle bundle = new CardColorBundle();
        bundle.cardColor = HSR_PINK;
        bundle.modId = MOD_NAME;
        bundle.bgColor =
                bundle.cardBackColor =
                        bundle.frameColor =
                                bundle.frameOutlineColor =
                                        bundle.descBoxColor =
                                                bundle.trailVfxColor =
                                                        bundle.glowColor = MY_COLOR;
        bundle.libraryType = StellaCharacter.PlayerLibraryEnum.HSR_PINK;
        bundle.attackBg = BG_ATTACK_512;
        bundle.skillBg = BG_SKILL_512;
        bundle.powerBg = BG_POWER_512;
        bundle.cardEnergyOrb = ENERGY_ORB;
        bundle.energyOrb = SMALL_ORB;
        bundle.attackBgPortrait = BG_ATTACK_1024;
        bundle.skillBgPortrait = BG_SKILL_1024;
        bundle.powerBgPortrait = BG_POWER_1024;
        bundle.energyOrbPortrait = BIG_ORB;
        bundle.setEnergyPortraitWidth(164);
        bundle.setEnergyPortraitHeight(164);
        BaseMod.addColor(bundle);
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new AndroidTestMod();
    }

    @Override
    public void receiveEditCards() {
        // This finds and adds all cards in the same package (or sub-package) as MyAbstractCard
        // along with marking all added cards as seen
        BaseMod.addDynamicVariable(new ToughnessReductionVariable());
        new AutoAdd(MOD_NAME)
                .packageFilter("hsrmod.cards")
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new StellaCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, STELLA_CHARACTER);
        HSRModConfig.getInstance().setProperties();
    }

    @Override
    public void receiveEditRelics() {
        if (HSRModConfig.addRelic) {
            new AutoAdd(MOD_NAME)
                    .packageFilter("hsrmod.relics")
                    .any(CustomRelic.class, new BiConsumer<AutoAdd.Info, CustomRelic>() {
                        @Override
                        public void accept(AutoAdd.Info info, CustomRelic relic) {
                            if (relic instanceof BaseRelic && ((BaseRelic) relic).hsrOnly) {
                                BaseMod.addRelicToCustomPool(relic, HSR_PINK);
                            } else {
                                BaseMod.addRelic(relic, RelicType.SHARED);
                            }
                            if (info.seen && relic != null) {
                                UnlockTracker.markRelicAsSeen(relic.relicId);
                            }
                        }
                    });
        }
    }

    public void receiveEditStrings() {
        updateLanguage();
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(MOD_NAME, CardStrings.class, "localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, CharacterStrings.class, "localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, RelicStrings.class, "localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, PowerStrings.class, "localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, EventStrings.class, "localization/" + lang + "/events.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, MonsterStrings.class, "localization/" + lang + "/monsters.json");
        BaseMod.loadCustomStringsFile(MOD_NAME, UIStrings.class, "localization/" + lang + "/ui.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        updateLanguage();

        String json = Gdx.files.internal("localization/" + lang + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword(MOD_NAME.toLowerCase(), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addSaveField("RewardEditor", RewardEditor.getInstance());
        HSRModConfig.getInstance().addConfigPanel();
        BaseMod.subscribe(PathSelectManager.Inst);
    }

    @Override
    public void receiveStartGame() {
        /*if (AbstractDungeon.player instanceof StellaCharacter) {
        }*/
    }

    public void updateLanguage() {
        if (language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
    }

    public static String makePath(String name) {
        if (name.endsWith("Power") || name.endsWith("Relic") || name.endsWith("Event")) {
            name = name.substring(0, name.length() - 5);
        }
        return MOD_NAME + ":" + name;
    }
}