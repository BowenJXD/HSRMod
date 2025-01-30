package hsrmod.dungeons;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import hsrmod.events.StelleAwakeEvent;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

import java.util.ArrayList;

public class Luofu extends CustomDungeon {
    public static String ID = HSRMod.makePath(Luofu.class.getSimpleName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String NAME;
    
    public Luofu() {
        super(NAME, ID, "HSRModResources/img/UI/panel.png", true, 4, 12, 10);
        setMainMusic("HSRModResources/audio/music/Lustrous Moonlight.mp3");
        // fight 1
        addTempMusic("Thundering Chariot", "HSRModResources/audio/music/Thundering Chariot.mp3");
        // fight 2
        addTempMusic("Into the Desolate", "HSRModResources/audio/music/Into the Desolate.mp3");
        // hoolay
        addTempMusic("Fatal Clash of Swordgraves", "HSRModResources/audio/music/Fatal Clash of Swordgraves.mp3");
        // deer
        addTempMusic("Deerstalker", "HSRModResources/audio/music/Deerstalker.mp3");
        // cirrus
        addTempMusic("Dancing Fantasms", "HSRModResources/audio/music/Dancing Fantasms.mp3");
        // shop
        addTempMusic("Paean of Indulgence", "HSRModResources/audio/music/Paean of Indulgence.mp3");
        // feixiao
        addTempMusic("Inner Beast Vanquished", "HSRModResources/audio/music/Inner Beast Vanquished.mp3");
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

    protected void initializeLevelSpecificChances() {shopRoomChance = 0.05F;
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
        
        monsters.add(new MonsterInfo(Encounter.DRAGONFISH_N_DRACOLION, 2.0F));
        monsters.add(new MonsterInfo(Encounter.MARA_STRUCK, 2.0F));
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
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        
        monsters.add(new MonsterInfo(Encounter.HOOLAY, 1.0F));
        monsters.add(new MonsterInfo(Encounter.SVAROG, 1.0F));
        monsters.add(new MonsterInfo(Encounter.BRONYA, 1.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    protected void initializeShrineList() {
    }

    protected void initializeEventList() {
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        NAME = TEXT[0];
    }
}
