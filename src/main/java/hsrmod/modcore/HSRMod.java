package hsrmod.modcore;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import hsrmod.characters.StellaCharacter;
import hsrmod.misc.ToughnessReductionVariable;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import static com.megacrit.cardcrawl.core.Settings.language;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;

@SpireInitializer // 加载mod的注解
public final class HSRMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber, PostInitializeSubscriber, StartGameSubscriber {
    public static String MOD_NAME = "HSRMod";

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
        BaseMod.addColor(HSR_PINK, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
        updateLanguage();
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new HSRMod();
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
        if (AbstractDungeon.player instanceof StellaCharacter) {
            BaseMod.removeCard(SadisticNature.ID, AbstractCard.CardColor.COLORLESS);
        }
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
        RelicTagField.destructible.set(RelicLibrary.getRelic(LizardTail.ID), true);
        RelicTagField.destructible.set(RelicLibrary.getRelic(MawBank.ID), true);
        RelicTagField.destructible.set(RelicLibrary.getRelic(Matryoshka.ID), true);

        String[] ecoRelicIDs = new String[]{
                MawBank.ID,
                GoldenIdol.ID,
                BloodyIdol.ID,
                MembershipCard.ID,
                OldCoin.ID,
                SmilingMask.ID,
                SneckoSkull.ID,
                TinyHouse.ID,
                Ectoplasm.ID,
        };
        for (String id : ecoRelicIDs) {
            RelicTagField.economic.set(RelicLibrary.getRelic(id), true);
        }
    }

    public void receiveEditStrings() {
        updateLanguage();
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(CardStrings.class, "HSRModResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "HSRModResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "HSRModResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "HSRModResources/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, "HSRModResources/localization/" + lang + "/events.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "HSRModResources/localization/" + lang + "/monsters.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "HSRModResources/localization/" + lang + "/ui.json");
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
    public void receivePostInitialize() {
        BaseMod.addSaveField("RewardEditor", RewardEditor.getInstance());
        HSRModConfig.getInstance().addConfigPanel();
    }

    @Override
    public void receiveStartGame() {
        /*if (AbstractDungeon.player instanceof StellaCharacter) {
        }*/
    }

    @Override
    public void receiveAddAudio() {
        addWav("Stelle1");
        addWav("Aventurine1");
        addWav("Firefly1-1");
        addWav("Firefly1-2");
        addWav("Firefly2");
        addWav("JingYuan1");
        addWav("Kafka2");
        addWav("Robin2");
        addWav("SlashedDream1");
        addWav("SlashedDream2");
        addWav("Feixiao2");
        addWav("Sparkle2");
        addWav("Gepard1");
        addWav("Argenti1");
        addWav("Seele1");
        addWav("SilverWolf1_0");
        addWav("SilverWolf1_1");
        addWav("ImbibitorLunae1_0");
        addWav("ImbibitorLunae1_1");
        addWav("Yunli1_0");
        addWav("Yunli1_1");
        addWav("Rappa1");
        addWav("Trailblazer8");
        addWav("TheHerta1");
        addWav("FuXuan2");
        addOgg("Jingliu1");
        addOgg("Jingliu2");
        addOgg("Mydei2");
        addOgg("Mydei3");

        for (int i = 1; i <= 10; i++) {
            addWav("TheGreatSeptimus_Day" + i);
        }
        for (int i = 1; i <= 5; i++) {
            addWav("TheGreatSeptimus_Crew" + i);
        }
        for (int i = 1; i <= 8; i++) {
            addWav("Phantylia_" + i);
        }
        for (int i = 0; i <= 8; i++) {
            addWav("Cocolia_" + i);
        }
        addWav("Gepard_0");
        addWav("Gepard_1");
        for (int i = 1; i <= 5; i++) {
            addWav("Hoolay" + i);
        }
        addWav("AurumatonGatekeeper_0");
        addWav("AurumatonGatekeeper_1");
        addOgg("ShapeShifter_0");
        addOgg("ShapeShifter_1");
        addOgg("HowlingCasket_0");
        addOgg("HowlingCasket_1");
        addOgg("AurumatonSpectralEnvoy_0");
        addOgg("AurumatonSpectralEnvoy_1");
        addOgg("TheAscended_0");
        addOgg("TheAscended_1");
        addOgg("Cirrus_0");
        addOgg("Cirrus_1");
        for (int i = 0; i <= 7; i++) {
            addOgg("ShadowOfFeixiao_" + i);
        }
        for (int i = 0; i <= 7; i++) {
            addOgg("AventurineOfStratagems_" + i);
        }
        for (int i = 0; i <= 5; i++) {
            addOgg("Sam_" + i);
        }
        for (int i = 0; i <= 5; i++) {
            addOgg("Yanqing_" + i);
        }
    }

    void addWav(String key) {
        BaseMod.addAudio(key, "HSRModResources/localization/" + lang + "/audio/" + key + ".wav");
    }

    void addOgg(String key) {
        BaseMod.addAudio(key, "HSRModResources/localization/" + lang + "/audio/" + key + ".ogg");
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