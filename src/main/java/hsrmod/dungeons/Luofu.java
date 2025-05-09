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

public class Luofu extends CustomDungeon {
    public static String ID = HSRMod.makePath(Luofu.class.getSimpleName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String NAME;
    
    public Luofu() {
        super(NAME, ID, "HSRModResources/img/UI/panel.png", true, 3, 12, 10);
        setMainMusic(PathDefine.MUSIC_PATH + "Lustrous Moonlight.mp3");
        // fight 1
        addTempMusic("Thundering Chariot", PathDefine.MUSIC_PATH + "Thundering Chariot.mp3");
        // fight 2
        addTempMusic("Into the Desolate", PathDefine.MUSIC_PATH + "Into the Desolate.mp3");
        // hoolay
        addTempMusic("Fatal Clash of Swordgraves", PathDefine.MUSIC_PATH + "Fatal Clash of Swordgraves.mp3");
        // deer
        addTempMusic("Deerstalker", PathDefine.MUSIC_PATH + "Deerstalker.mp3");
        // cirrus
        addTempMusic("Dancing Fantasms", PathDefine.MUSIC_PATH + "Dancing Fantasms.mp3");
        // yanqing
        addTempMusic("Gleaming Clash", PathDefine.MUSIC_PATH + "Gleaming Clash.mp3");
        // shop
        addTempMusic("Paean of Indulgence", PathDefine.MUSIC_PATH + "Paean of Indulgence.mp3");
        // feixiao
        addTempMusic("Inner Beast Vanquished", PathDefine.MUSIC_PATH + "Inner Beast Vanquished.mp3");
    }

    public Luofu(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Luofu(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new LuofuScene();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.125F;
        } else {
            cardUpgradedChance = 0.25F;
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
        
        monsters.add(new MonsterInfo(Encounter.DRAGONFISH_N_FLOATINGS, 2.0F));
        monsters.add(new MonsterInfo(Encounter.TWO_MARA_STRUCK, 2.0F));
        monsters.add(new MonsterInfo(Encounter.HOUNDS, 2.0F));
        monsters.add(new MonsterInfo(Encounter.CLOUD_KNIGHTS_PATROLLERS, 2.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        
        monsters.add(new MonsterInfo(Encounter.AUROMATON_GATEKEEPER, 2.0F));
        monsters.add(new MonsterInfo(Encounter.SHAPE_SHIFTER, 2.0F));
        monsters.add(new MonsterInfo(Encounter.HOWLING_CASKET, 2.0F));
        monsters.add(new MonsterInfo(Encounter.AURUMATON_SPECTRAL_ENVOY, 2.0F));
        monsters.add(new MonsterInfo(Encounter.MALEFIC_APE, 2.0F));
        monsters.add(new MonsterInfo(Encounter.THE_ASCENDED, 2.0F));
        monsters.add(new MonsterInfo(Encounter.TWIGS, 2.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        
        monsters.add(new MonsterInfo(Encounter.HOOLAY, 1.0F));
        monsters.add(new MonsterInfo(Encounter.ABUNDANT_EBON_DEER, 1.0F));
        monsters.add(new MonsterInfo(Encounter.CIRRUS, 1.0F));
        monsters.add(new MonsterInfo(Encounter.YANQING, 1.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    @Override
    protected void initializeShrineList() {
        super.initializeShrineList();
    }

    protected void initializeEventList() {
        eventList.add("Addict");
        eventList.add("Back to Basics");
        eventList.add("Beggar");
        eventList.add("Colosseum");
        eventList.add("Cursed Tome");
        eventList.add("Drug Dealer");
        eventList.add("Forgotten Altar");
        eventList.add("Ghosts");
        eventList.add("Masked Bandits");
        eventList.add("Nest");
        eventList.add("The Library");
        eventList.add("The Mausoleum");
        // eventList.add("Vampires");
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        NAME = TEXT[0];
        fadeColor = Color.valueOf("1e0f0aff");
        sourceFadeColor = Color.valueOf("1e0f0aff");
    }
}
