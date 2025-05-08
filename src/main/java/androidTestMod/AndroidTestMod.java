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

import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;
import static androidTestMod.characters.StellaCharacter.PlayerColorEnum.STELLA_CHARACTER;
import static com.megacrit.cardcrawl.core.Settings.language;

public final class AndroidTestMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, StartGameSubscriber {
    public static String MOD_NAME = "AndroidTestMod";

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

        /*String json = AssetLoader.getString(MOD_NAME, "localization/" + lang + "/keywords.json");
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword(MOD_NAME.toLowerCase(), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }*/
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "弹射X", "弹射" }, "执行X次：对随机敌人触发效果。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "耗能X", "耗能" }, "消耗X充能才能打出。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "充能" }, "有一些牌需要消耗一定充能才能发动。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "追击" }, "有 #y追击 关键词的牌为追击牌。追击牌在达成特定条件后，将不消耗费用自动打出（随机选择目标）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "持续伤害" }, "包括裂伤，灼烧，触电，风化。受效果增益。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破" }, "通过削韧的伤害将韧性从正数削减至 #b0 及以下的动作视为击破。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "韧性" }, "受到的伤害-X%。受到属性伤害后，移除削韧值数量的韧性，属性伤害的削韧值为后面括号里的数字（如：物理 #b2 ）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破状态" }, "将韧性削减至 #b0 及以下时施加的状态。受到的伤害+ #b50% ，造成的伤害- #b25% 。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破效果" }, "冰： #b2 击破伤害，施加 #y冻结 。 NL 物理： #b4 击破伤害，施加 #y裂伤 。 NL 火： #b4 击破伤害，施加 #y灼烧 。 NL 雷： #b2 击破伤害，施加 #y触电 。 NL 风： #b3 击破伤害，施加 #y风化 。 NL 量子： #b1 击破伤害，施加 #y纠缠 。 NL 虚数： #b1 击破伤害，施加 #y禁锢 。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "冻结" }, "回合开始时，若为普通/精英/首领敌人，且层数不小于 #b1 / #b2 / #b3 层，则移除所有层，并跳过回合。 NL 受到伤害后，移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "裂伤" }, "持续伤害。回合开始时触发：受到生命值上限 #b5% （至多为 #b10 ）的伤害（物理 #b1 ），移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "灼烧" }, "持续伤害。回合开始时触发：受到 #b6 点伤害（火 #b1 ），移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "触电" }, "持续伤害。回合开始时触发：受到 #b7 点伤害（雷 #b1 ），移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "风化" }, "持续伤害。回合开始时触发：受到 #b2X 点伤害（风 #b1 ）（X为层数，至多计入 #b5 层），移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "禁锢" }, "造成的伤害- #b33% ，回合结束时移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "纠缠" }, "受到伤害时叠加 #b1 层，至多 #b5 层。回合开始时移除所有层，受到移除层数数量* #b3 点伤害。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破特攻" }, "造成的击破伤害增加X。对有击破状态的敌人造成的持续伤害+X。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破效率" }, "属性伤害的削韧值增加 #b50% 。回合结束时移除 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "击破伤害" }, "受 #y击破特攻 增幅，不受 #y力量 增幅。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "超击破伤害" }, "基数为削韧值的 #y击破伤害 。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "临时生命" }, "于战斗中临时存在的生命（不受上限限制）。可以作为生命失去。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "回味" }, "打出追击牌造成伤害后，对目标造成X点伤害，然后叠加 #b1 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "怀疑" }, "受到的持续伤害+X%。至多叠加 #b99 层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "反震" }, "一次性失去一半及以上的格挡时，移除 #b1 层，获得 #b1 张伤害值为X的「反震」（X为失去的格挡数）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "孢子" }, "至多 #b6 层，回合开始时移除 #b1 层。成为攻击牌的目标后爆裂：移除所有层，受到X*X的伤害（风 #b1 ）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "会心" }, "至多叠加 #b8 层。造成的伤害+X。被攻击后移除所有层。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "罐中脑" }, "消耗充能后，移除 #b1 层，回复等量的充能。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "格挡返还" }, "对其造成伤害后，获得X层格挡。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "残梦" }, "回合结束时，若不小于 #b9 层，则消耗 #b9 层，施放 #r残梦尽染，一刀缭断 ：弹射 #b3 次：造成 #b5 点伤害（雷 #b1 ）；最后对所有敌人造成 #b5+X 点伤害（雷 #b1 ）（X为其负面效果数）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "盲注" }, "叠加后，若不小于 #b7 层，则消耗 #b7 层，获得 #y宾果！ 。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "格挡返还" }, "每当受到攻击时，你获得X点格挡。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "柔韧" }, "受到攻击时，获得X点格挡。每触发一次，获得的格挡值都会增加。在你的回合开始时重新变回X点。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "余像" }, "你每打出一张牌，得到X点格挡。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "多重护甲" }, "在你的回合结束时获得X点格挡。受到攻击伤害而失去生命时，层数- #b1 。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "坏死" }, "触发并叠加1层坏死（回合开始时，移除1层并触发：若有X点临时生命，失去之。）。");
        BaseMod.addKeyword(AndroidTestMod.MOD_NAME, new String[] { "珠露" }, "回合结束时，移除所有层触发：对随机敌人造成X点固定伤害。");
    }

    @Override
    public void receivePostInitialize() {
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