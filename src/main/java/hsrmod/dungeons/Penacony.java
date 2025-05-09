package hsrmod.dungeons;

import actlikeit.dungeons.CustomDungeon;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.PathDefine;

import java.util.ArrayList;

public class Penacony extends CustomDungeon {
    public static String ID = HSRMod.makePath(Penacony.class.getSimpleName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String NAME;

    public Penacony() {
        super(NAME, ID, "HSRModResources/img/UI/panel.png", true, 2, 12, 10);
        setMainMusic(PathDefine.MUSIC_PATH + "The Big Sleep.mp3");
        // fight 1
        addTempMusic("The Player on The Other Side", PathDefine.MUSIC_PATH + "The Player on The Other Side.mp3");
        // fight 2
        addTempMusic("Fair Play", PathDefine.MUSIC_PATH + "Fair Play.mp3");
        // fight 3
        addTempMusic("Against the Day", PathDefine.MUSIC_PATH + "Against the Day.mp3");
        // shop
        addTempMusic("Full House", PathDefine.MUSIC_PATH + "Full House.mp3");
        // Something Unto Death
        addTempMusic("Return of None", PathDefine.MUSIC_PATH + "Return of None.mp3");
        // Sam
        addTempMusic("Nevermore", PathDefine.MUSIC_PATH + "Nevermore.mp3");
        // Aventurine
        addTempMusic("Hell Is Preferable to Nihility", PathDefine.MUSIC_PATH + "Hell Is Preferable to Nihility.mp3");
        // past, present and future
        addTempMusic("The Past, Present, and Eternal Show", PathDefine.MUSIC_PATH + "The Past, Present, and Eternal Show.mp3");
        // bananas
        addTempMusic("Bananas", PathDefine.MUSIC_PATH + "Bananas.mp3");
        // ???
        addTempMusic("The Fool Always Rings Twice", PathDefine.MUSIC_PATH + "The Fool Always Rings Twice.mp3");
        // campfire 1
        addTempMusic("If I Can Stop One Heart From Breaking (Encore)", PathDefine.MUSIC_PATH + "If I Can Stop One Heart From Breaking (Encore).mp3");
        // campfire 2
        addTempMusic("Im Anfang war das Wort", PathDefine.MUSIC_PATH + "Im Anfang war das Wort.mp3");
        // Skaracabaz
        addTempMusic("Aberrant Receptacle", PathDefine.MUSIC_PATH + "Aberrant Receptacle.mp3");
        
        allowFinalActRewards();
    }
    
    public Penacony(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Penacony(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new PenaconyScene();
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.12F; // orig 0.08F
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    public String getBodyText() {
        return TEXT[2];
    }

    public String getOptionText() {
        return TEXT[3];
    }

    protected void generateMonsters() {
        this.generateWeakEnemies(this.weakpreset);
        this.generateStrongEnemies(this.strongpreset);
        this.generateElites(this.elitepreset);
    }

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();

        monsters.add(new MonsterInfo(Encounter.HOUND_N_DOMESCREEN, 2.0F));
        monsters.add(new MonsterInfo(Encounter.TWO_FLOATINGS_N_HEARTBREAKER, 2.0F));
        monsters.add(new MonsterInfo(Encounter.MEMORY_ZONE_MEME, 2.0F));

        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();

        monsters.add(new MonsterInfo(Encounter.SWEET_GORILLA, 2.0F));
        monsters.add(new MonsterInfo(Encounter.BEYOND_OVERCOOKED, 2.0F));
        monsters.add(new MonsterInfo(Encounter.SHELL_OF_FADED_RAGE, 2.0F));
        monsters.add(new MonsterInfo(Encounter.PAST, 2.0F));
        monsters.add(new MonsterInfo(Encounter.PRESENT, 2.0F));
        monsters.add(new MonsterInfo(Encounter.TOMORROW, 2.0F));
        monsters.add(new MonsterInfo(Encounter.COMPANY, 2.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();

        monsters.add(new MonsterInfo(Encounter.SOMETHING_UNTO_DEATH, 1.0F));
        monsters.add(new MonsterInfo(Encounter.BLAZNANA_MONKEY_TRICK, 1.0F));
        monsters.add(new MonsterInfo(Encounter.THE_PAST_PRESENT_AND_ETERNAL_SHOW, 1.0F));
        monsters.add(new MonsterInfo(Encounter.AVENTURINE_OF_STRATAGEMS, 1.0F));
        monsters.add(new MonsterInfo(Encounter.SAM, 1.0F));

        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    @Override
    protected void initializeBoss() {
        super.initializeBoss();
        /*bossList.add(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
        if (AbstractDungeon.ascensionLevel >= 20)
            bossList.add(Encounter.BOREHOLE_PLANETS_OLD_CRATER);*/
    }
    
    @Override
    protected void initializeShrineList() {
        super.initializeShrineList();
    }

    protected void initializeEventList() {
        eventList.add("Falling");
        eventList.add("MindBloom");
        eventList.add("The Moai Head");
        eventList.add("Mysterious Sphere");
        // eventList.add("SensoryStone");
        eventList.add("Tomb of Lord Red Mask");
        eventList.add("Winding Halls");
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        NAME = TEXT[0];
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");
    }
}
