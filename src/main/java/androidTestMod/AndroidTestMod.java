package androidTestMod;

import androidTestMod.cards.base.*;
import androidTestMod.cards.common.*;
import androidTestMod.cards.rare.*;
import androidTestMod.cards.uncommon.*;
import androidTestMod.cardsV2.*;
import androidTestMod.cardsV2.Curse.Frozen;
import androidTestMod.cardsV2.Curse.Imprison;
import androidTestMod.cardsV2.Erudition.*;
import androidTestMod.cardsV2.Paths.*;
import androidTestMod.cardsV2.Preservation.*;
import androidTestMod.cardsV2.Propagation.*;
import androidTestMod.cardsV2.TheHunt.*;
import androidTestMod.characters.StellaCharacter;
import androidTestMod.relics.boss.HerosTriumphantReturn;
import androidTestMod.relics.boss.MasterOfDreamMachinations;
import androidTestMod.relics.boss.Plaguenest;
import androidTestMod.relics.common.*;
import androidTestMod.relics.rare.*;
import androidTestMod.relics.shop.ARuanPouch;
import androidTestMod.relics.shop.CavitySystemModel;
import androidTestMod.relics.shop.TemporaryStake;
import androidTestMod.relics.shop.WrittenInWater;
import androidTestMod.relics.special.*;
import androidTestMod.relics.starter.*;
import androidTestMod.relics.uncommon.*;
import androidTestMod.utils.PathSelectManager;
import androidTestMod.utils.RewardEditor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.android.mods.interfaces.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;
import static com.megacrit.cardcrawl.core.Settings.language;

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
        BaseMod.addCard(new Gepard1());
        BaseMod.addCard(new FuXuan1());
        BaseMod.addCard(new Macrosegregation());
        BaseMod.addCard(new ResonanceTransfer());
        BaseMod.addCard(new TheArchitects());
        BaseMod.addCard(new Trailblazer7());
        BaseMod.addCard(new Trailblazer8());
        BaseMod.addCard(new MetastaticField());
        BaseMod.addCard(new ThisIsMe());
        BaseMod.addCard(new ConcertForTwo());
        BaseMod.addCard(new DayOneOfMyNewLife());
        BaseMod.addCard(new DestinysThreadsForewoven());
        BaseMod.addCard(new Amber());
        BaseMod.addCard(new Burst());
        BaseMod.addCard(new Concentration());
        BaseMod.addCard(new Quake());
        BaseMod.addCard(new Preservation());
        BaseMod.addCard(new Qingque1());
        BaseMod.addCard(new Yukong1());
        BaseMod.addCard(new Hanya1());
        BaseMod.addCard(new ImbibitorLunae1());
        BaseMod.addCard(new Sparkle1());
        BaseMod.addCard(new Sparkle2());
        BaseMod.addCard(new IntersegmentalMembrane());
        BaseMod.addCard(new SporeDischarge());
        BaseMod.addCard(new ExcitatoryGland());
        BaseMod.addCard(new SerfOfCalamity());
        BaseMod.addCard(new FungalPustule());
        BaseMod.addCard(new Sparkle3());
        BaseMod.addCard(new ImbibitorLunae2());
        BaseMod.addCard(new SwarmKingProgeny());
        BaseMod.addCard(new PhenolCompounds());
        BaseMod.addCard(new Propagation());
        BaseMod.addCard(new Luocha1());
        BaseMod.addCard(new Abundance());
        BaseMod.addCard(new Qingque2());
        BaseMod.addCard(new Herta1());
        BaseMod.addCard(new March7th1());
        BaseMod.addCard(new March7th2());
        BaseMod.addCard(new Yanqing1());
        BaseMod.addCard(new Yanqing2());
        BaseMod.addCard(new Clara1());
        BaseMod.addCard(new Himeko1());
        BaseMod.addCard(new JingYuan1());
        BaseMod.addCard(new TopazNumby2());
        BaseMod.addCard(new TopazNumby3());
        BaseMod.addCard(new DrRatio1());
        BaseMod.addCard(new DrRatio2());
        BaseMod.addCard(new DrRatio3());
        BaseMod.addCard(new Aventurine1());
        BaseMod.addCard(new Aventurine2());
        BaseMod.addCard(new Aventurine3());
        BaseMod.addCard(new Robin1());
        BaseMod.addCard(new Robin2());
        BaseMod.addCard(new Jade1());
        BaseMod.addCard(new Yunli1());
        BaseMod.addCard(new Sunday1());
        BaseMod.addCard(new BattlefieldMagician());
        BaseMod.addCard(new SlaughterhouseNo4RestInPeace());
        BaseMod.addCard(new UselessScholar());
        BaseMod.addCard(new BountyHunter());
        BaseMod.addCard(new ChampionsDinnerCatsCradle());
        BaseMod.addCard(new VineyardAgainstTheNight());
        BaseMod.addCard(new Elation());
        BaseMod.addCard(new Sushang2());
        BaseMod.addCard(new Sushang3());
        BaseMod.addCard(new Xueyi1());
        BaseMod.addCard(new Gallagher1());
        BaseMod.addCard(new RuanMei1());
        BaseMod.addCard(new RuanMei2());
        BaseMod.addCard(new Trailblazer5());
        BaseMod.addCard(new Trailblazer6());
        BaseMod.addCard(new Boothill1());
        BaseMod.addCard(new Boothill2());
        BaseMod.addCard(new Firefly1());
        BaseMod.addCard(new Firefly2());
        BaseMod.addCard(new Lingsha1());
        BaseMod.addCard(new Rappa1());
        BaseMod.addCard(new Fugue1());
        BaseMod.addCard(new Symbiote());
        BaseMod.addCard(new RegressionInequalityOfAnnihilation());
        BaseMod.addCard(new CourtOfHomogeneity());
        BaseMod.addCard(new VoyageMonitor());
        BaseMod.addCard(new DisruptivePulse());
        BaseMod.addCard(new PreBattleCaregiver());
        BaseMod.addCard(new NightBeyondPyre());
        BaseMod.addCard(new StarcrusherWorker());
        BaseMod.addCard(new CleftSingularity());
        BaseMod.addCard(new Destruction());
        BaseMod.addCard(new March7th3());
        BaseMod.addCard(new Trailblazer1());
        BaseMod.addCard(new March7th0());
        BaseMod.addCard(new Danheng0());
        BaseMod.addCard(new Himeko0());
        BaseMod.addCard(new Welt0());
        BaseMod.addCard(new Trailblazer2());
        BaseMod.addCard(new Trailblazer3());
        BaseMod.addCard(new AstralExpress());
        BaseMod.addCard(new Sampo1());
        BaseMod.addCard(new Pela1());
        BaseMod.addCard(new Serval1());
        BaseMod.addCard(new Luka1());
        BaseMod.addCard(new Guinaifen1());
        BaseMod.addCard(new Welt1());
        BaseMod.addCard(new SilverWolf1());
        BaseMod.addCard(new Kafka1());
        BaseMod.addCard(new Kafka2());
        BaseMod.addCard(new BlackSwan1());
        BaseMod.addCard(new BlackSwan2());
        BaseMod.addCard(new Acheron1());
        BaseMod.addCard(new Jiaoqiu1());
        BaseMod.addCard(new Jiaoqiu2());
        BaseMod.addCard(new TheDoubtfulFourfoldRoot());
        BaseMod.addCard(new AllThingsArePossible());
        BaseMod.addCard(new AnExtraPersonsDiary());
        BaseMod.addCard(new ThePathless());
        BaseMod.addCard(new ReignOfKeys());
        BaseMod.addCard(new FuneralOfSensoryPursuivant());
        BaseMod.addCard(new CallOfTheWilderness());
        BaseMod.addCard(new CommonMortal());
        BaseMod.addCard(new NightWatch());
        BaseMod.addCard(new SinThirster());
        BaseMod.addCard(new OfferingsOfDeception());
        BaseMod.addCard(new Nihility());
        BaseMod.addCard(new March7th4());
        BaseMod.addCard(new Bronya1());
        BaseMod.addCard(new Seele1());
        BaseMod.addCard(new Danheng1());
        BaseMod.addCard(new Moze1());
        BaseMod.addCard(new Moze2());
        BaseMod.addCard(new March7th5());
        BaseMod.addCard(new Feixiao1());
        BaseMod.addCard(new Feixiao2());
        BaseMod.addCard(new RadiantSupreme());
        BaseMod.addCard(new SovereignSkybreaker());
        BaseMod.addCard(new SolemnSnare());
        BaseMod.addCard(new EmpyreanImperium());
        BaseMod.addCard(new BorderstarGunslinger());
        BaseMod.addCard(new GalaxyRanger());
        BaseMod.addCard(new DisasterHaltingMechanism());
        BaseMod.addCard(new TheHunt());
        BaseMod.addCard(new Tingyun1());
        BaseMod.addCard(new Huohuo1());
        BaseMod.addCard(new Argenti1());
        BaseMod.addCard(new CivilizationCorrespondent());
        BaseMod.addCard(new Jade2());
        BaseMod.addCard(new SilverheartGuards());
        BaseMod.addCard(new SMR2Amygdala());
        BaseMod.addCard(new Kolchis());
        BaseMod.addCard(new Aggregator());
        BaseMod.addCard(new BCI34GrayMatter());
        BaseMod.addCard(new MakeTheWorldClamor());
        BaseMod.addCard(new Tribbie1());
        BaseMod.addCard(new TheHerta1());
        BaseMod.addCard(new TerminalNirvana());
        BaseMod.addCard(new Erudition());
        BaseMod.addCard(new Imprison());
        BaseMod.addCard(new Frozen());
        BaseMod.addCard(new NightOnTheMilkyWay());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new StellaCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, STELLA_CHARACTER);
    }

    @Override
    public void receiveEditRelics() {
            BaseMod.addRelic(new PomPomBlessing());
            BaseMod.addRelic(new GalacticBat());
            BaseMod.addRelic(new WarpingCompoundEye());
            BaseMod.addRelic(new CasketOfInaccuracy());
            BaseMod.addRelic(new AmbergrisCheese());
            BaseMod.addRelic(new TheParchmentThatAlwaysEats());
            BaseMod.addRelic(new TheDoctorsRobe());
            BaseMod.addRelic(new SocietyTicket());
            BaseMod.addRelic(new RecordFromBeyondTheSky());
            BaseMod.addRelic(new EntropicDie());
            BaseMod.addRelic(new AngelTypeIOUDispenser());
            BaseMod.addRelic(new BlackHoleTrap());
            BaseMod.addRelic(new RubertEmpireMechanicalCogwheel());
            BaseMod.addRelic(new RubertEmpireMechanicalLever());
            BaseMod.addRelic(new RubertEmpireMechanicalPiston());
            BaseMod.addRelic(new PriceOfPeace());
            BaseMod.addRelic(new ShatterboneBlade());
            BaseMod.addRelic(new BeaconColoringPaste());
            BaseMod.addRelic(new SuperOverlordSpinningTop());
            BaseMod.addRelic(new GeniusSocietysDangerousGossip());
            BaseMod.addRelic(new PunklordianBalance());
            BaseMod.addRelic(new GreenFingers());
            BaseMod.addRelic(new FruitOfTheAlienTree());
            BaseMod.addRelic(new Dreams0110());
            BaseMod.addRelic(new VileMechanicalSatellite900());
            BaseMod.addRelic(new VoidWickTrimmer());
            BaseMod.addRelic(new FaithBond());
            BaseMod.addRelic(new DimensionReductionDice());
            BaseMod.addRelic(new FortuneGlue());
            BaseMod.addRelic(new ShiningTrapezohedronDie());
            BaseMod.addRelic(new GoldCoinOfDiscord());
            BaseMod.addRelic(new SilverCoinOfDiscord());
            BaseMod.addRelic(new SpaceTimePrism());
            BaseMod.addRelic(new CosmicBigLotto());
            BaseMod.addRelic(new InterastralBigLotto());
            BaseMod.addRelic(new IllusoryAutomaton());
            BaseMod.addRelic(new IndecipherableBox());
            BaseMod.addRelic(new KingOfSponges());
            BaseMod.addRelic(new PunklordianRegards());
            BaseMod.addRelic(new JellyfishOnTheStaircase());
            BaseMod.addRelic(new RobeOfTheBeauty());
            BaseMod.addRelic(new ByAnyMeansNecessary());
            BaseMod.addRelic(new HerosTriumphantReturn());
            BaseMod.addRelic(new Plaguenest());
            BaseMod.addRelic(new WrittenInWater());
            BaseMod.addRelic(new ARuanPouch());
            BaseMod.addRelic(new TemporaryStake());
            BaseMod.addRelic(new CavitySystemModel());
            BaseMod.addRelic(new RubertEmpireDifferenceMachine());
            BaseMod.addRelic(new ThePinkestCollision());
            BaseMod.addRelic(new ThalanToxiFlame());
            BaseMod.addRelic(new Pineapple());
            BaseMod.addRelic(new InsectWeb());
            BaseMod.addRelic(new WaxOfElation());
            BaseMod.addRelic(new WaxOfDestruction());
            BaseMod.addRelic(new WaxOfNihility());
            BaseMod.addRelic(new WaxOfPropagation());
            BaseMod.addRelic(new WaxOfPreservation());
            BaseMod.addRelic(new WaxOfTheHunt());
            BaseMod.addRelic(new WaxOfErudition());
            BaseMod.addRelic(new WaxOfAbundance());
            BaseMod.addRelic(new TrailblazeTimer());
            BaseMod.addRelic(new TheAshblazingGrandDuke());
            BaseMod.addRelic(new IronCavalryAgainstTheScourge());
            BaseMod.addRelic(new PrisonerInDeepConfinement());
            BaseMod.addRelic(new PasserbyOfWanderingCloud());
            BaseMod.addRelic(new KnightOfPurityPalace());
            BaseMod.addRelic(new MusketeerOfWildWheat());
            BaseMod.addRelic(new TheWindSoaringValorous());
            BaseMod.addRelic(new LongevousDisciple());
            BaseMod.addRelic(new MasterOfDreamMachinations());
            BaseMod.addRelic(new ParallelUniverseWalkieTalkie());
            BaseMod.addRelic(new Revitalization310());
            BaseMod.addRelic(new ChanceJailbreak());
            BaseMod.addRelic(new RoadToComets());
            BaseMod.addRelic(new DreamdiveCan());
            BaseMod.addRelic(new SilentSong());
            BaseMod.addRelic(new TenLightYearsForesight());
            BaseMod.addRelic(new HimekoRelic());
            BaseMod.addRelic(new WeltRelic());
            BaseMod.addRelic(new DanhengRelic());
            BaseMod.addRelic(new March7thRelic());
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