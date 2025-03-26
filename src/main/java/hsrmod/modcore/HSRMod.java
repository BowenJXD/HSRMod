package hsrmod.modcore;

import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.badlogic.gdx.graphics.Color;
import hsrmod.cards.uncommon.SilverWolf1;
import hsrmod.characters.StellaCharacter;
import hsrmod.dungeons.Belobog;
import hsrmod.dungeons.Luofu;
import hsrmod.dungeons.Penacony;
import hsrmod.events.*;
import hsrmod.misc.*;
import hsrmod.monsters.Bonus.KingTrashcan;
import hsrmod.monsters.Bonus.LordlyTrashcan;
import hsrmod.monsters.Exordium.*;
import hsrmod.monsters.Bonus.SequenceTrotter;
import hsrmod.monsters.TheBeyond.*;
import hsrmod.monsters.TheCity.*;
import hsrmod.patches.OtherModFixes;
import hsrmod.patches.RelicTagField;
import hsrmod.powers.enemyOnly.PerformancePointPower;
import hsrmod.relics.BaseRelic;
import hsrmod.relics.common.AngelTypeIOUDispenser;
import hsrmod.relics.special.*;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.core.Settings.language;
import static hsrmod.characters.StellaCharacter.PlayerColorEnum.*;

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
        CustomIconHelper.addCustomIcon(ChargeIcon.get());
        // This finds and adds all cards in the same package (or sub-package) as MyAbstractCard
        // along with marking all added cards as seen
        BaseMod.addDynamicVariable(new ToughnessReductionVariable());
        new AutoAdd(MOD_NAME)
                .packageFilter("hsrmod.cards")
                .setDefaultSeen(true)
                .cards();
        if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player instanceof StellaCharacter) {
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
                    .any(CustomRelic.class, (info, relic) -> {
                        if (relic instanceof BaseRelic && ((BaseRelic) relic).hsrOnly) {
                            BaseMod.addRelicToCustomPool(relic, HSR_PINK);
                        } else {
                            BaseMod.addRelic(relic, RelicType.SHARED);
                        }
                        if (info.seen && relic != null) {
                            UnlockTracker.markRelicAsSeen(relic.relicId);
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
        OtherModFixes.setRelics();
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
        addMonsters();
        if (HSRModConfig.addEnemy) {
            BaseMod.addSaveField("BonusManager", BonusManager.getInstance());
        }
        if (HSRModConfig.addEvent) {
            addEvents();
        }
        checkSignatureUnlock();
    }

    @Override
    public void receiveStartGame() {
        /*if (AbstractDungeon.player instanceof StellaCharacter) {
        }*/
    }

    public void addEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(RuanMeiEvent.ID), RuanMeiEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 6)
                .eventType(EventUtils.EventType.ONE_TIME)
                .playerClass(STELLA_CHARACTER)
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(CosmicCrescendoEvent.ID), CosmicCrescendoEvent.class)
                .dungeonID(Belobog.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfElation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(TavernEvent.ID), TavernEvent.class)
                .dungeonID(Luofu.ID)
                .spawnCondition(() -> !ModHelper.hasRelic(AngelTypeIOUDispenser.ID))
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfDestruction.ID))
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(IOUDispenserEvent.ID), IOUDispenserEvent.class)
                .dungeonID(Luofu.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfNihility.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(LonelyBeautyBugsOneEvent.ID), LonelyBeautyBugsOneEvent.class)
                .dungeonIDs(Belobog.ID, Luofu.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfPreservation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(SlumberingOverlordEvent.ID), SlumberingOverlordEvent.class)
                .dungeonID(Penacony.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfPropagation.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(RockPaperScissorsEvent.ID), RockPaperScissorsEvent.class)
                .dungeonID(Luofu.ID)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfTheHunt.ID))
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(DoubleLotteryEvent.ID), DoubleLotteryEvent.class)
                // .bonusCondition(() -> ModHelper.hasRelic(WaxOfErudition.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(WaxManufacturerEvent.ID), WaxManufacturerEvent.class)
                .dungeonID(Belobog.ID)
                // .bonusCondition(() -> WaxRelic.getSelectedPathTag(AbstractDungeon.player.relics) != WaxManufacturerEvent.getMostCommonTag(AbstractDungeon.player.masterDeck))
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(ThreeLittlePigsEvent.ID), ThreeLittlePigsEvent.class)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(ImperialLegacyEvent.ID), ImperialLegacyEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 50)
                .dungeonID(Penacony.ID)
                // .eventType(EventUtils.EventType.ONE_TIME)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(AceTrashDiggerEvent.ID), AceTrashDiggerEvent.class)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(PineappleBreadEvent.ID), PineappleBreadEvent.class)
                .eventType(EventUtils.EventType.ONE_TIME)
                .spawnCondition(() -> !ModHelper.hasRelic(Pineapple.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(YuQingtuEvent.ID), YuQingtuEvent.class)
                .spawnCondition(() -> !ModHelper.hasRelic(ThePinkestCollision.ID) && !ModHelper.hasRelic(ThalanToxiFlame.ID))
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(IPMBShoppingMallEvent.ID), IPMBShoppingMallEvent.class)
                .eventType(EventUtils.EventType.SHRINE)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(TrashSymphonyEvent.ID), TrashSymphonyEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 50)
                .dungeonID(Penacony.ID)
                // .eventType(EventUtils.EventType.ONE_TIME)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(CulinaryColosseumEvent.ID), CulinaryColosseumEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 50)
                .dungeonID(Penacony.ID)
                // .eventType(EventUtils.EventType.ONE_TIME)
                .endsWithRewardsUI(true)
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(RobotSalesTerminalEvent.ID), RobotSalesTerminalEvent.class)
                .eventType(EventUtils.EventType.SHRINE)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(TheRelicFixerEvent.ID), TheRelicFixerEvent.class)
                .spawnCondition(() -> AbstractDungeon.player.relics.stream().anyMatch(r -> RelicTagField.destructible.get(r)))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(WeAreCowboysEvent.ID), WeAreCowboysEvent.class)
                .dungeonID(Penacony.ID)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(InsectNestEvent.ID), InsectNestEvent.class)
                .dungeonID(Penacony.ID)
                .spawnCondition(() -> !ModHelper.hasRelic(InsectWeb.ID))
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(FleaMarketEvent.ID), FleaMarketEvent.class)
                .dungeonID(Belobog.ID)
                .spawnCondition(() -> AbstractDungeon.player.gold >= 50)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(ApesSuchAsYouEvent.ID), ApesSuchAsYouEvent.class)
                .dungeonID(Luofu.ID)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(ShoppingStreetEvent.ID), ShoppingStreetEvent.class)
                .dungeonID(Belobog.ID)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(SelfAnnihilatorEvent.ID), SelfAnnihilatorEvent.class)
                .dungeonID(Luofu.ID)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(KnightsOfBeautyEvent.ID), KnightsOfBeautyEvent.class)
                .spawnCondition(() -> AbstractDungeon.eventRng.random(99) < 50)
                .eventType(EventUtils.EventType.ONE_TIME)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(TheReturningHeliobusEvent.ID), TheReturningHeliobusEvent.class)
                .dungeonID(Penacony.ID)
                .endsWithRewardsUI(true)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(FalseFutureEvent.ID), FalseFutureEvent.class)
                .dungeonIDs(Belobog.ID, Luofu.ID, Penacony.ID, Exordium.ID, TheCity.ID, TheBeyond.ID)
                .spawnCondition(() -> !ModHelper.hasRelic(TowatCards.ID))
                .eventType(EventUtils.EventType.ONE_TIME)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(ShoppingChannelEvent.ID), ShoppingChannelEvent.class)
                .dungeonID(Luofu.ID)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(GeniusSocietyEvent.ID), GeniusSocietyEvent.class)
                .dungeonID(Belobog.ID)
                .create());
        BaseMod.addEvent(new AddEventParams.Builder(HSRMod.makePath(CheatCode1Event.ID), CheatCode1Event.class)
                .dungeonIDs(Belobog.ID, Luofu.ID)
                .create());

        // =========================== Event Monsters ===========================

        BaseMod.addMonster(Encounter.PARASITE_N_SLAVER, () -> new MonsterGroup(new AbstractMonster[]{
                new ShelledParasite(),
                new SlaverRed(130.0F, 20F)
        }));
        BaseMod.addMonster(Encounter.THREE_LIL_PIGS, () -> new MonsterGroup(new AbstractMonster[]{
                new SequenceTrotter(-400, AbstractDungeon.monsterRng.random(-15, 15), 0),
                new SequenceTrotter(-100, AbstractDungeon.monsterRng.random(-15, 15), 2),
                new SequenceTrotter(+200, AbstractDungeon.monsterRng.random(-15, 15), 1),
        }));
        BaseMod.addMonster(Encounter.THREE_LIL_PIGS_SLOW, () -> new MonsterGroup(new AbstractMonster[]{
                new SequenceTrotter(-400, AbstractDungeon.monsterRng.random(-15, 15), 0),
                new SequenceTrotter(-100, AbstractDungeon.monsterRng.random(-15, 15), 1),
                new SequenceTrotter(+200, AbstractDungeon.monsterRng.random(-15, 15), 0),
        }));
        BaseMod.addMonster(Encounter.THREE_LIL_PIGS_FAST, () -> new MonsterGroup(new AbstractMonster[]{
                new SequenceTrotter(-400, AbstractDungeon.monsterRng.random(-15, 15), 1),
                new SequenceTrotter(-100, AbstractDungeon.monsterRng.random(-15, 15), 2),
                new SequenceTrotter(+200, AbstractDungeon.monsterRng.random(-15, 15), 1),
        }));
        BaseMod.addMonster(Encounter.RPS_1, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomFloating(-300, AbstractDungeon.monsterRng.random(15, 30)),
                new IlluminationDragonfish(-100, AbstractDungeon.monsterRng.random(0, 15)),
                Encounter.getRandomFloating(100, AbstractDungeon.monsterRng.random(15, 30)),
        }));
        BaseMod.addMonster(Encounter.RPS_2, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomMaraStruck(-300, AbstractDungeon.monsterRng.random(-15, 15)).modifyHpByPercent(1.25f),
                Encounter.getRandomMaraStruck(0, AbstractDungeon.monsterRng.random(-15, 15)).modifyHpByPercent(1.25f),
                Encounter.getRandomMaraStruck(300, AbstractDungeon.monsterRng.random(-15, 15)).modifyHpByPercent(1.25f),
        }));
        BaseMod.addMonster(Encounter.TAVERN_1, () -> new MonsterGroup(new AbstractMonster[]{
                new Direwolf(0, AbstractDungeon.monsterRng.random(-15, 15)).modifyHpByPercent(1.5f),
        }));
        BaseMod.addMonster(Encounter.TAVERN_2, () -> new MonsterGroup(new AbstractMonster[]{
                new Grizzly(-100, 0).modifyHpByPercent(1.5f),
        }));
        BaseMod.addMonster(Encounter.TAVERN_3, () -> new MonsterGroup(new AbstractMonster[]{
                new Grizzly(-200, 0).process(m -> m.specialAs = false),
                new Direwolf(300, 0),
        }));
        BaseMod.addMonster(Encounter.TRASH_SYMPHONY_1, () -> new MonsterGroup(new AbstractMonster[]{
                new LordlyTrashcan(-250, 0),
                new Heartbreaker(0, 0),
                new LordlyTrashcan(250, 0),
        }));
        BaseMod.addMonster(Encounter.TRASH_SYMPHONY_2, () -> new MonsterGroup(new AbstractMonster[]{
                new KingTrashcan(0, 0).modifyHpByPercent(2).modifyToughnessByPercent(2),
        }));
        BaseMod.addMonster(Encounter.CULINARY_COLOSSEUM_1, () -> new MonsterGroup(new AbstractMonster[]{
                new MrDomescreen(-250, 0),
                new LordlyTrashcan(0, 0),
                new MrDomescreen(250, 0),
        }));
        BaseMod.addMonster(Encounter.CULINARY_COLOSSEUM_2, () -> new MonsterGroup(new AbstractMonster[]{
                new SweetGorilla(-175, 0),
                new LordlyTrashcan(350, 0)
        }));
        BaseMod.addMonster(Encounter.CULINARY_COLOSSEUM_3, () -> new MonsterGroup(new AbstractMonster[]{
                new LordlyTrashcan(-250, 0),
                new KingTrashcan(0, 0).modifyHpByPercent(1.5f).modifyToughnessByPercent(1.5f),
                new LordlyTrashcan(250, 0),
        }));
        BaseMod.addMonster(Encounter.WE_ARE_COWBOYS_1, () -> new MonsterGroup(new AbstractMonster[]{
                new Heartbreaker(-250, 0).modifyHpByPercent(0.75f),
                new ShellOfFadedRage(100, 0).modifyHpByPercent(0.75f)
        }));
        BaseMod.addMonster(Encounter.WE_ARE_COWBOYS_2, () -> new MonsterGroup(new AbstractMonster[]{
                new MrDomescreen(-250, 0).modifyHpByPercent(0.75f),
                new BeyondOvercooked(100, 0).modifyHpByPercent(0.75f)
        }));
        BaseMod.addMonster(Encounter.WE_ARE_COWBOYS_3, () -> new MonsterGroup(new AbstractMonster[]{
                new InsatiableVanity(-250, 0).modifyHpByPercent(0.75f),
                new PastConfinedAndCaged(100, 0).modifyHpByPercent(0.75f)
        }));
        BaseMod.addMonster(Encounter.APES_SUCH_AS_YOU, () -> new MonsterGroup(new AbstractMonster[]{
                new GoldenHound(-350, 0).modifyHpByPercent(0.75f),
                new MaleficApe(-50, 0).modifyHpByPercent(0.75f),
                new WoodenLupus(250, 0).modifyHpByPercent(0.75f),
        }));
        BaseMod.addMonster(Encounter.SELF_ANNIHILATOR, () -> new MonsterGroup(new AbstractMonster[]{
                new DecayingShadow(-250, 0),
                new GuardianShadow(150, 0),
        }));
        BaseMod.addMonster(Encounter.THE_RETURNING_HELIOBUS, () -> new MonsterGroup(new AbstractMonster[]{
                new Cirrus(true).modifyHp(400).setPreBattleAction(m -> {
                    m.specialAs = true;
                    m.addToBot(new ApplyPowerAction(m, m, new FadingPower(m, 4)));
                    m.addToBot(new ApplyPowerAction(m, m, new TimeWarpPower(m)));
                })
        }));
        BaseMod.addMonster(Encounter.CHEAT_CODE_2, () -> new MonsterGroup(new AbstractMonster[]{
                new TeamLeader(-200, 0).modifyHpByPercent(1.5f).modifyToughnessByPercent(1.5f).setPreBattleAction(m -> {
                    m.addToBot(new ApplyPowerAction(m, m, new PerformancePointPower(m, 4)));
                    m.addToBot(new RollMoveAction(m));
                    ModHelper.addToBotAbstract(m::createIntent);
                }),
        }));
    }

    public void addMonsters() {
        Belobog belobog = new Belobog();
        Luofu luofu = new Luofu();
        Penacony penacony = new Penacony();

        // =========================== Boss ===========================

        belobog.addBoss(Encounter.END_OF_THE_ETERNAL_FREEZE, () -> new MonsterGroup(new AbstractMonster[]{
                new Cocolia()
        }), "HSRModResources/img/monsters/EndOfTheEternalFreeze.png", "HSRModResources/img/monsters/BossOutline.png");
        belobog.addBoss(Encounter.DESTRUCTIONS_BEGINNING, () -> new MonsterGroup(new AbstractMonster[]{
                new AntimatterEngine(),
                new DawnsLeftHand(),
                new DisastersRightHand(),
        }), "HSRModResources/img/monsters/DestructionsBeginning.png", "HSRModResources/img/monsters/BossOutline.png");

        luofu.addBoss(Encounter.DIVINE_SEED, () -> new MonsterGroup(new AbstractMonster[]{
                new Phantylia(),
        }), "HSRModResources/img/monsters/DivineSeed.png", "HSRModResources/img/monsters/BossOutline.png");
        luofu.addBoss(Encounter.INNER_BEASTS_BATTLEFIELD, () -> new MonsterGroup(new AbstractMonster[]{
                new WorldpurgeTail(-400, 220),
                new PlaneshredClaws(-160, 220),
                new NebulaDevourer(160, 220),
                new ShadowOfFeixiao(),
        }), "HSRModResources/img/monsters/InnerBeastsBattlefield.png", "HSRModResources/img/monsters/BossOutline.png");

        penacony.addBoss(Encounter.SALUTATIONS_OF_ASHEN_DREAMS, () -> new MonsterGroup(new AbstractMonster[]{
                new EchoOfFadedDreams(0, -500F, 50.0F),
                new TheGreatSeptimus(),
                new EchoOfFadedDreams(1, 300F, 50.0F)
        }), "HSRModResources/img/monsters/SalutationsOfAshenDreams.png", "HSRModResources/img/monsters/BossOutline.png");
        penacony.addBoss(Encounter.BOREHOLE_PLANETS_OLD_CRATER, () -> new MonsterGroup(new AbstractMonster[]{
                new Skaracabaz(),
        }), "HSRModResources/img/monsters/BoreholePlanetsOldCrater.png", "HSRModResources/img/monsters/BossOutline.png");

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
        BaseMod.addMonster(Encounter.ABUNDANT_EBON_DEER, () -> new MonsterGroup(new AbstractMonster[]{
                new AbundantEbonDeer()
        }));
        BaseMod.addMonster(Encounter.CIRRUS, () -> new MonsterGroup(new AbstractMonster[]{
                new Cirrus()
        }));
        BaseMod.addMonster(Encounter.YANQING, () -> new MonsterGroup(new AbstractMonster[]{
                new Yanqing()
        }));

        BaseMod.addMonster(Encounter.SOMETHING_UNTO_DEATH, () -> new MonsterGroup(new AbstractMonster[]{
                new SomethingUntoDeath()
        }));
        BaseMod.addMonster(Encounter.BLAZNANA_MONKEY_TRICK, () -> new MonsterGroup(new AbstractMonster[]{
                new DreamweaverBananAdvisor(-500, AbstractDungeon.monsterRng.random(-15, 15), true),
                new Assistanana(-270, AbstractDungeon.monsterRng.random(-15, 15), false),
                new CharmonyBananAdvisor(0, AbstractDungeon.monsterRng.random(-15, 15), true),
                new FortuneBananAdvisor(300, AbstractDungeon.monsterRng.random(50, 75), ModHelper.specialAscension(AbstractMonster.EnemyType.ELITE)),
        }));
        BaseMod.addMonster(Encounter.THE_PAST_PRESENT_AND_ETERNAL_SHOW, () -> new MonsterGroup(new AbstractMonster[]{
                new PresentInebriatedInRevelry(-400, 0).modifyHpByPercent(1.5f).modifyToughnessByPercent(1.5f),
                new TomorrowInHarmoniousChords(-100, 0).modifyHpByPercent(1.5f).modifyToughnessByPercent(1.5f),
                new PastConfinedAndCaged(200, 0).modifyHpByPercent(1.5f).modifyToughnessByPercent(1.5f),
        }));
        BaseMod.addMonster(Encounter.AVENTURINE_OF_STRATAGEMS, () -> new MonsterGroup(new AbstractMonster[]{
                new AventurineOfStratagems()
        }));
        BaseMod.addMonster(Encounter.SAM, () -> new MonsterGroup(new AbstractMonster[]{
                new Sam()
        }));

        // =========================== Stronger ===========================

        BaseMod.addMonster(Encounter.GRIZZLY, () -> new MonsterGroup(new AbstractMonster[]{
                new Grizzly(-175, 0)
        }));
        BaseMod.addMonster(Encounter.FRIGID_PROWLER, () -> new MonsterGroup(new AbstractMonster[]{
                new FrigidProwler(0, 0)
        }));
        BaseMod.addMonster(Encounter.GUARDIAN_SHADOW, () -> new MonsterGroup(new AbstractMonster[]{
                new GuardianShadow(-100, 0)
        }));
        BaseMod.addMonster(Encounter.DECAYING_SHADOW, () -> new MonsterGroup(new AbstractMonster[]{
                new DecayingShadow(-100, 0)
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
                new AurumatonGatekeeper(-150, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.SHAPE_SHIFTER, () -> new MonsterGroup(new AbstractMonster[]{
                new ShapeShifter(-100, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.HOWLING_CASKET, () -> new MonsterGroup(new AbstractMonster[]{
                new SableclawWolftrooper(-300, AbstractDungeon.monsterRng.random(-15, 15)),
                new HowlingCasket(-50, 0),
                new EclipseWolftrooper(200, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.AURUMATON_SPECTRAL_ENVOY, () -> new MonsterGroup(new AbstractMonster[]{
                new WraithWarden(-300, AbstractDungeon.monsterRng.random(-15, 15)),
                new AurumatonSpectralEnvoy(-50, AbstractDungeon.monsterRng.random(-15, 15)),
                new WraithWarden(200, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.MALEFIC_APE, () -> new MonsterGroup(new AbstractMonster[]{
                new MaleficApe(0, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.THE_ASCENDED, () -> new MonsterGroup(new AbstractMonster[]{
                new TheAscended(0, AbstractDungeon.monsterRng.random(15, 50)),
        }));
        BaseMod.addMonster(Encounter.TWIGS, () -> new MonsterGroup(new AbstractMonster[]{
                new TwigOfWintryWind(-250, AbstractDungeon.monsterRng.random(-15, 15)),
                new TwigOfMarpleLeaf(-50, AbstractDungeon.monsterRng.random(-15, 15)),
                new TwigOfGloriousBlooms(150, AbstractDungeon.monsterRng.random(-15, 15)),
        }));

        BaseMod.addMonster(Encounter.SWEET_GORILLA, () -> new MonsterGroup(new AbstractMonster[]{
                new SweetGorilla(-175, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.BEYOND_OVERCOOKED, () -> new MonsterGroup(new AbstractMonster[]{
                new BeyondOvercooked(0, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.SHELL_OF_FADED_RAGE, () -> new MonsterGroup(new AbstractMonster[]{
                new ShellOfFadedRage(0, AbstractDungeon.monsterRng.random(-15, 15))
        }));
        BaseMod.addMonster(Encounter.PAST, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomDreamjoltTroupe(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new PastConfinedAndCaged(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.PRESENT, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomDreamjoltTroupe(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new PresentInebriatedInRevelry(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.TOMORROW, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomDreamjoltTroupe(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new TomorrowInHarmoniousChords(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.COMPANY, () -> new MonsterGroup(new AbstractMonster[]{
                new TeamLeader(-200, AbstractDungeon.monsterRng.random(-15, 15)),
        }));

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

        BaseMod.addMonster(Encounter.DRAGONFISH_N_FLOATINGS, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomFloating(-300, AbstractDungeon.monsterRng.random(15, 30)),
                new IlluminationDragonfish(-100, AbstractDungeon.monsterRng.random(0, 15)),
                Encounter.getRandomFloating(100, AbstractDungeon.monsterRng.random(15, 30)),
        }));
        BaseMod.addMonster(Encounter.TWO_MARA_STRUCK, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomMaraStruck(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                Encounter.getRandomMaraStruck(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.HOUNDS, () -> new MonsterGroup(new AbstractMonster[]{
                new GoldenHound(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new WoodenLupus(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.CLOUD_KNIGHTS_PATROLLERS, () -> new MonsterGroup(new AbstractMonster[]{
                new CloudKnightsPatroller(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new CloudKnightsPatroller(100, AbstractDungeon.monsterRng.random(-15, 15)),
        }));

        BaseMod.addMonster(Encounter.HOUND_N_DOMESCREEN, () -> new MonsterGroup(new AbstractMonster[]{
                new MrDomescreen(-400, AbstractDungeon.monsterRng.random(-15, 15)),
                new BubbleHound(-100, AbstractDungeon.monsterRng.random(-15, 15)),
                new MrDomescreen(200, AbstractDungeon.monsterRng.random(-15, 15)),
        }));
        BaseMod.addMonster(Encounter.TWO_FLOATINGS_N_HEARTBREAKER, () -> new MonsterGroup(new AbstractMonster[]{
                Encounter.getRandomFloatingPenacony(-250, AbstractDungeon.monsterRng.random(100, 115)),
                new Heartbreaker(0, AbstractDungeon.monsterRng.random(-15, 15)),
                Encounter.getRandomFloatingPenacony(250, AbstractDungeon.monsterRng.random(100, 115)),
        }));
        BaseMod.addMonster(Encounter.MEMORY_ZONE_MEME, () -> new MonsterGroup(new AbstractMonster[]{
                new SomethingInTheMirror(-200, AbstractDungeon.monsterRng.random(-15, 15)),
                new SomethingInTheMirror(200, AbstractDungeon.monsterRng.random(-15, 15)),
                new InsatiableVanity(-200, AbstractDungeon.monsterRng.random(300, 315)),
                new InsatiableVanity(200, AbstractDungeon.monsterRng.random(300, 315)),
        }));

        // ==================================================================

        belobog.addAct("Exordium");
        luofu.addAct("TheCity");
        penacony.addAct("TheBeyond");
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
    
    void checkSignatureUnlock() {
        SilverWolf1.checkUnlockSign();
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