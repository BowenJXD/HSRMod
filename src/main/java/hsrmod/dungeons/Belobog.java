package hsrmod.dungeons;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import hsrmod.events.StelleAwakeEvent;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;

import java.util.ArrayList;

public class Belobog extends CustomDungeon {
    public static String ID = HSRMod.makePath(Belobog.class.getSimpleName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String NAME;
    
    public Belobog() {
        super(NAME, ID, "HSRModResources/img/UI/panel.png", true, 4, 12, 10);
        setMainMusic("HSRModResources/audio/music/Embers.mp3");
        addTempMusic("Braving the Cold", "HSRModResources/audio/music/Braving the Cold.mp3");
        addTempMusic("Kindling", "HSRModResources/audio/music/Kindling.mp3");
        if ((BaseMod.hasModID("spireTogether:") || Settings.isTrial)) {
            onEnterEvent(StelleAwakeEvent.class);
        }
    }

    public Belobog(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Belobog(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new BelobogScene();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.25F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        cardUpgradedChance = 0.0F;
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
        
        monsters.add(new MonsterInfo(Encounter.TWO_AUTOMATONS, 2.0F));
        // monsters.add(new MonsterInfo(Encounter.SHADEWALKERS, 2.0F));
        monsters.add(new MonsterInfo(Encounter.VAGRANT, 2.0F));
        monsters.add(new MonsterInfo(Encounter.MASK_N_SPAWNS, 2.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        
        monsters.add(new MonsterInfo(Encounter.GRIZZLY, 2.0F));
        monsters.add(new MonsterInfo(Encounter.FRIGID_PROWLER, 2.0F));
        monsters.add(new MonsterInfo(Encounter.GUARDIAN_SHADOW, 2.0F));
        monsters.add(new MonsterInfo(Encounter.DECAYING_SHADOW, 2.0F));
        monsters.add(new MonsterInfo(Encounter.SILVERMANE_LIEUTENANT, 2.0F));
        monsters.add(new MonsterInfo(Encounter.VAGRANTS, 2.0F));
        monsters.add(new MonsterInfo(Encounter.STORMBRINGER, 2.0F));
        monsters.add(new MonsterInfo(Encounter.DIREWOLF, 2.0F));
        
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        
        monsters.add(new MonsterInfo(Encounter.GEPARD, 1.0F));
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
