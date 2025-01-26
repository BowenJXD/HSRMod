package hsrmod.modcore;

import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.eventUtil.AddEventParams;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.badlogic.gdx.graphics.Color;
import hsrmod.characters.StellaCharacter;
import hsrmod.dungeons.Belobog;
import hsrmod.events.*;
import hsrmod.misc.BonusManager;
import hsrmod.misc.ChargeIcon;
import hsrmod.misc.Encounter;
import hsrmod.misc.ToughnessReductionVariable;
import hsrmod.monsters.Exordium.*;
import hsrmod.monsters.SequenceTrotter;
import hsrmod.monsters.TheBeyond.*;
import hsrmod.monsters.TheCity.*;
import hsrmod.relics.special.Pineapple;
import hsrmod.relics.special.ThalanToxiFlame;
import hsrmod.relics.special.ThePinkestCollision;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.megacrit.cardcrawl.core.Settings.language;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.*;

@SpireInitializer // 加载mod的注解
public class HSRMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber, PostInitializeSubscriber, StartGameSubscriber {
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

    public static SpireConfig config;
    public static ModInfo info;
    public static boolean addRelic = true;
    public static boolean addEvent = true;
    public static boolean addEnemy = true;
    public static boolean removeOtherEnemies = true;

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
        CustomIconHelper.addCustomIcon(ChargeIcon.get());
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
        try {
            Properties defaults = new Properties();
            defaults.setProperty("addRelic", Boolean.toString(true));
            defaults.setProperty("addEvent", Boolean.toString(true));
            defaults.setProperty("addEnemy", Boolean.toString(true));
            defaults.setProperty("removeOtherEnemies", Boolean.toString(true));
            config = new SpireConfig(MOD_NAME, HSRMod.makePath("Config"), defaults);
            config.load();
            addRelic = config.getBool("addRelic");
            addEvent = config.getBool("addEvent");
            addEnemy = config.getBool("addEnemy");
            removeOtherEnemies = config.getBool("removeOtherEnemies");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveEditRelics() {
        if (addRelic)
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
        addConfigPanel();
        BaseMod.addSaveField("RewardEditor", RewardEditor.getInstance());
        BaseMod.removeCard(SadisticNature.ID, AbstractCard.CardColor.COLORLESS);
        BaseMod.removeRelic(RelicLibrary.getRelic(ChemicalX.ID));
        if (addEnemy) {
            addMonsters();
            BaseMod.addSaveField("BonusManager", BonusManager.getInstance());
        }
        if (addEvent) addEvents();
    }

    @Override
    public void receiveStartGame() {
        /*if (AbstractDungeon.player instanceof StellaCharacter) {
        }*/
    }

    public void addEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(RuanMeiEvent.ID, RuanMeiEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 10)
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(CosmicCrescendoEvent.ID, CosmicCrescendoEvent.class)
                .dungeonID(Belobog.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfElation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(TavernEvent.ID, TavernEvent.class)
                .dungeonID(TheCity.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfDestruction.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(IOUDispenserEvent.ID, IOUDispenserEvent.class)
                .dungeonID(TheCity.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfNihility.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(LonelyBeautyBugsOneEvent.ID, LonelyBeautyBugsOneEvent.class)
                .dungeonIDs(Belobog.ID, TheCity.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfPreservation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(SlumberingOverlordEvent.ID, SlumberingOverlordEvent.class)
                .dungeonID(TheBeyond.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfPropagation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(RockPaperScissorsEvent.ID, RockPaperScissorsEvent.class)
                .dungeonID(TheCity.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfTheHunt.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(DoubleLotteryEvent.ID, DoubleLotteryEvent.class)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfErudition.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(WaxManufacturerEvent.ID, WaxManufacturerEvent.class)
                .dungeonID(Belobog.ID)
                // .bonusCondition(() -> WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics) != WaxManufacturerEvent.getMostCommonTag(AbstractDungeon.player.masterDeck))
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(ThreeLittlePigsEvent.ID, ThreeLittlePigsEvent.class)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(ImperialLegacyEvent.ID, ImperialLegacyEvent.class)
                .dungeonID(TheBeyond.ID)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(AceTrashDiggerEvent.ID, AceTrashDiggerEvent.class)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(PineappleBreadEvent.ID, PineappleBreadEvent.class)
                .spawnCondition(() -> !ModHelper.hasRelic(Pineapple.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(YuQingtuEvent.ID, YuQingtuEvent.class)
                .spawnCondition(() -> !ModHelper.hasRelic(ThePinkestCollision.ID) && !ModHelper.hasRelic(ThalanToxiFlame.ID))   
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(IPMBShoppingMallEvent.ID, IPMBShoppingMallEvent.class)
                .dungeonID(TheBeyond.ID)
                .create());
        
        // =========================== Event Monsters ===========================

        BaseMod.addMonster(Encounter.PARASITE_N_SLAVER, () -> new MonsterGroup(new AbstractMonster[]{
                new ShelledParasite(),
                new SlaverRed(130.0F, 20F)
        }));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo(Encounter.PARASITE_N_SLAVER, 0.0F));
        BaseMod.addMonster(Encounter.THREE_LIL_PIGS, () -> new MonsterGroup(new AbstractMonster[]{
                new SequenceTrotter(-400, AbstractDungeon.monsterRng.random(-15, 15), 0),
                new SequenceTrotter(-100, AbstractDungeon.monsterRng.random(-15, 15), 2),
                new SequenceTrotter(+200, AbstractDungeon.monsterRng.random(-15, 15), 1),
        }));
        BaseMod.addStrongMonsterEncounter(Belobog.ID, new MonsterInfo(Encounter.THREE_LIL_PIGS, 0.0F));
    }

    public void addMonsters() {
        Belobog belobog = new Belobog();

        // =========================== Boss ===========================

        belobog.addBoss(Encounter.END_OF_THE_ETERNAL_FREEZE, Cocolia::new, "HSRModResources/img/monsters/EndOfTheEternalFreeze.png", "HSRModResources/img/monsters/BossOutline.png");
        belobog.addBoss(Encounter.DESTRUCTIONS_BEGINNING, () -> new MonsterGroup(new AbstractMonster[]{
                new DawnsLeftHand(),
                new AntimatterEngine(),
                new DisastersRightHand()
        }), "HSRModResources/img/monsters/DestructionsBeginning.png", "HSRModResources/img/monsters/BossOutline.png");
        
        BaseMod.addMonster(Encounter.DIVINE_SEED, () -> new MonsterGroup(new AbstractMonster[]{
                new Phantylia(),
        }));
        BaseMod.addBoss(TheCity.ID, Encounter.DIVINE_SEED, "HSRModResources/img/monsters/DivineSeed.png", "HSRModResources/img/monsters/BossOutline.png");
        
        BaseMod.addMonster(Encounter.SALUTATIONS_OF_ASHEN_DREAMS, () -> new MonsterGroup(new AbstractMonster[]{
                new EchoOfFadedDreams(0, -500F, 50.0F),
                new TheGreatSeptimus(),
                new EchoOfFadedDreams(1, 300F, 50.0F)
        }));
        BaseMod.addBoss(TheBeyond.ID, Encounter.SALUTATIONS_OF_ASHEN_DREAMS, "HSRModResources/img/monsters/SalutationsOfAshenDreams.png", "HSRModResources/img/monsters/BossOutline.png");

        // =========================== Elite ===========================
        
        BaseMod.addMonster(Encounter.GEPARD, () -> new MonsterGroup(new AbstractMonster[]{
                new Gepard()
        }));
        BaseMod.addMonster(Encounter.BRONYA, () -> new MonsterGroup(new AbstractMonster[]{
                new Bronya()
        }));
        BaseMod.addMonster(Encounter.SVAROG, () -> new MonsterGroup(new AbstractMonster[]{
                new Svarog()
        }));
        
        BaseMod.addMonster(Encounter.HOOLAY, () -> new MonsterGroup(new AbstractMonster[]{
                new Hoolay()
        }));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(Encounter.HOOLAY, 3.0F));
        
        BaseMod.addMonster(Encounter.SOMETHING_UNTO_DEATH, () -> new MonsterGroup(new AbstractMonster[]{
                new SomethingUntoDeath()
        }));
        BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo(Encounter.SOMETHING_UNTO_DEATH, 3.0F));
        
        // =========================== Stronger ===========================
        
        BaseMod.addMonster(Encounter.GRIZZLY, () -> new MonsterGroup(new AbstractMonster[]{
                new Grizzly()
        }));
        BaseMod.addMonster(Encounter.FRIGID_PROWLER, () -> new MonsterGroup(new AbstractMonster[]{
                new FrigidProwler()
        }));
        BaseMod.addMonster(Encounter.GUARDIAN_SHADOW, () -> new MonsterGroup(new AbstractMonster[]{
                new GuardianShadow()
        }));
        BaseMod.addMonster(Encounter.DECAYING_SHADOW, () -> new MonsterGroup(new AbstractMonster[]{
                new DecayingShadow()
        }));
        BaseMod.addMonster(Encounter.SILVERMANE_LIEUTENANT, () -> new MonsterGroup(new AbstractMonster[]{
                new SilvermaneLieutenant(-100, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.VAGRANTS, () -> new MonsterGroup(new AbstractMonster[]{
                new Vagrant(-250, AbstractDungeon.monsterRng.random(-15, 30)),
                new Vagrant(0, AbstractDungeon.monsterRng.random(-15, 30)),
        }));
        BaseMod.addMonster(Encounter.STORMBRINGER, () -> new MonsterGroup(new AbstractMonster[]{
                new Windspawn(-300, AbstractDungeon.monsterRng.random(180, 200)),
                new Stormbringer(0, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.DIREWOLF, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomAutomaton(-300, AbstractDungeon.monsterRng.random(-15, 15)),
                new Direwolf(0, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        
        
        BaseMod.addMonster(Encounter.AUROMATON_GATEKEEPER, () -> new MonsterGroup(new AbstractMonster[]{
                new AurumatonGatekeeper()
        }));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo(Encounter.AUROMATON_GATEKEEPER, 3.0F));
        
        
        BaseMod.addMonster(Encounter.SWEET_GORILLA, () -> new MonsterGroup(new AbstractMonster[]{
                new SweetGorilla()
        }));
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo(Encounter.SWEET_GORILLA, 3.0F));
        
        // =========================== Normal ===========================
        
        BaseMod.addMonster(Encounter.TWO_AUTOMATONS, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomAutomaton(-250, AbstractDungeon.monsterRng.random(-15, 15)),
                Encounter.getRandomAutomaton(0, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.SHADEWALKERS, () -> new MonsterGroup(new AbstractMonster[]{
                new EverwinterShadewalker(-250, AbstractDungeon.monsterRng.random(-15, 15)),
                new IncinerationShadewalker(0, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.VAGRANT, () -> new MonsterGroup(new AbstractMonster[]{
                new Vagrant(0, AbstractDungeon.monsterRng.random(-15, 30)),
        }));
        BaseMod.addMonster(Encounter.MASK_N_SPAWNS, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomSpawn(-300, AbstractDungeon.monsterRng.random(180, 200)),
                new MaskOfNoThought(-100, AbstractDungeon.monsterRng.random(150, 180)),
                Encounter.getRandomSpawn(100, AbstractDungeon.monsterRng.random(180, 200)),
        }));
        
        
        BaseMod.addMonster(Encounter.DRAGONFISH_N_DRACOLION, () -> new MonsterGroup(new AbstractMonster[]{
                new ObedientDracolion(-300, AbstractDungeon.monsterRng.random(0, 15)),
                new IlluminationDragonfish(-100, AbstractDungeon.monsterRng.random(-15, 0)),
                new ObedientDracolion(100, AbstractDungeon.monsterRng.random(0, 15)),
        }));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo(Encounter.DRAGONFISH_N_DRACOLION, 3.0F));
        
        
        BaseMod.addMonster(Encounter.HOUND_N_DOMESCREEN, () -> new MonsterGroup(new AbstractMonster[]{
                new MrDomescreen(-400, AbstractDungeon.monsterRng.random(-15, 15)),
                new BubbleHound(-100, AbstractDungeon.monsterRng.random(-15, 15)),
                new MrDomescreen(200, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonsterEncounter(TheBeyond.ID, new MonsterInfo(Encounter.HOUND_N_DOMESCREEN, 3.0F));
        
        // ==================================================================
        
        belobog.addAct("Exordium");
    }

    @Override
    public void receiveAddAudio() {
        addAudio("Stelle1");
        addAudio("Aventurine1");
        addAudio("Firefly1-1");
        addAudio("Firefly1-2");
        addAudio("Firefly2");
        addAudio("JingYuan1");
        addAudio("Kafka2");
        addAudio("Robin2");
        addAudio("SlashedDream1");
        addAudio("SlashedDream2");
        addAudio("Feixiao2");
        addAudio("Sparkle2");
        addAudio("Gepard1");
        addAudio("Argenti1");

        for (int i = 1; i <= 10; i++) {
            addAudio("TheGreatSeptimus_Day" + i);
        }
        for (int i = 1; i <= 5; i++) {
            addAudio("TheGreatSeptimus_Crew" + i);
        }
        for (int i = 1; i <= 8; i++) {
            addAudio("Phantylia_" + i);
        }
        for (int i = 0; i <= 8; i++) {
            addAudio("Cocolia_" + i);
        }
        addAudio("Gepard_0");
        addAudio("Gepard_1");
        for (int i = 1; i <= 5; i++) {
            addAudio("Hoolay" + i);
        }
        addAudio("AurumatonGatekeeper_0");
        addAudio("AurumatonGatekeeper_1");
    }

    void addAudio(String id) {
        BaseMod.addAudio(id, "HSRModResources/localization/" + lang + "/audio/" + id + ".wav");
    }

    void addConfigPanel() {
        loadSettings();
        ModPanel panel = new ModPanel();
        String[] buttonLanguage = null;
        if (language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT)
            buttonLanguage = new String[]{"加入遗物", "加入事件", "加入敌人", "移除原版敌人", "部分设定需要重启并重开游戏才能生效"};
        else 
            buttonLanguage = new String[]{"Add Relic", "Add Event", "Add Enemy", "Remove Other Enemies", "Some settings need to restart and reopen the game to take effect"};
        Texture badgeTexture = ImageMaster.loadImage("HSRModResources/img/char/badge.png");
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, Arrays.stream(info.Authors).findFirst().orElse(""), info.Description, panel);
        
        panel.addUIElement(new ModLabel(buttonLanguage[4], 400.0F, 800.0F, panel, (me) -> {}));
        ModLabeledToggleButton addRelicButton = new ModLabeledToggleButton(buttonLanguage[0], 400.0F, 700.0F, Color.WHITE, FontHelper.buttonLabelFont, addRelic, panel, (label) -> {}, (button) -> {
            addRelic = button.enabled;
            try {
                config.setBool("addRelic", addRelic);
                config.save();
            } catch (IOException e) { e.printStackTrace(); }
        });
        panel.addUIElement(addRelicButton);
        
        ModLabeledToggleButton addEventButton = new ModLabeledToggleButton(buttonLanguage[1], 400.0F, 600.0F, Color.WHITE, FontHelper.buttonLabelFont, addEvent, panel, (label) -> {}, (button) -> {
            addEvent = button.enabled;
            try {
                config.setBool("addEvent", addEvent);
                config.save();
            } catch (IOException e) { e.printStackTrace(); }
        });
        panel.addUIElement(addEventButton);
        
        ModLabeledToggleButton addEnemyButton = new ModLabeledToggleButton(buttonLanguage[2], 400.0F, 500.0F, Color.WHITE, FontHelper.buttonLabelFont, addEnemy, panel, (label) -> {}, (button) -> {
            addEnemy = button.enabled;
            try {
                config.setBool("addEnemy", addEnemy);
                config.save();
            } catch (IOException e) { e.printStackTrace(); }
        });
        panel.addUIElement(addEnemyButton);
        
        ModLabeledToggleButton removeOtherEnemiesButton = new ModLabeledToggleButton(buttonLanguage[3], 400.0F, 400.0F, Color.WHITE, FontHelper.buttonLabelFont, removeOtherEnemies, panel, (label) -> {}, (button) -> {
            removeOtherEnemies = button.enabled;
            try {
                config.setBool("removeOtherEnemies", removeOtherEnemies);
                config.save();
            } catch (IOException e) { e.printStackTrace(); }
        });
        panel.addUIElement(removeOtherEnemiesButton);
    }

    private static void loadSettings() {
        try {
            config = new SpireConfig(MOD_NAME, makePath("Config"));
            config.load();
        } catch (Exception var1) {
            Exception ex = var1;
            logger.catching(ex);
        }
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = (AnnotationDB) Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null) {
                return false;
            } else {
                Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
                return initializers.contains(HSRMod.class.getName());
            }
        }).findFirst();
        if (infos.isPresent()) {
            info = (ModInfo) infos.get();
            MOD_NAME = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    public void updateLanguage() {
        if (language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
    }

    public static String makePath(String name) {
        if (name.contains("Power")) {
            name = name.replace("Power", "");
        } else if (name.contains("Relic")) {
            name = name.replace("Relic", "");
        } else if (name.contains("Event")) {
            name = name.replace("Event", "");
        }
        return MOD_NAME + ":" + name;
    }

    static {
        loadModInfo();
    }
}