package hsrmod.misc;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.monsters.Exordium.*;
import hsrmod.monsters.TheBeyond.*;
import hsrmod.monsters.TheCity.*;

public class Encounter {
    // ======================== EVENT ========================
    
    public static final String PARASITE_N_SLAVER = "Parasite and Red Slaver";
    public static final String THREE_LIL_PIGS = "Three Little Pigs";
    public static final String RPS_1 = "Rock Paper Scissors 1";
    public static final String RPS_2 = "Rock Paper Scissors 2";
    public static final String TAVERN_1 = "Tavern 1";
    public static final String TAVERN_2 = "Tavern 2";
    public static final String TAVERN_3 = "Tavern 3";
    public static final String TRASH_SYMPHONY_1 = "Trash Symphony 1";
    public static final String TRASH_SYMPHONY_2 = "Trash Symphony 2";
    public static final String CULINARY_COLOSSEUM_1 = "CulinaryColosseum 1";
    public static final String CULINARY_COLOSSEUM_2 = "CulinaryColosseum 2";
    public static final String CULINARY_COLOSSEUM_3 = "CulinaryColosseum 3";
    
    // ======================== BOSS ========================
    
    public static final String END_OF_THE_ETERNAL_FREEZE = "End of the Eternal Freeze";
    public static final String DESTRUCTIONS_BEGINNING = "Destruction's Beginning";
    public static final String DIVINE_SEED = "Divine Seed";
    public static final String INNER_BEASTS_BATTLEFIELD = "Inner Beast's Battlefield";
    public static final String SALUTATIONS_OF_ASHEN_DREAMS = "Salutations of Ashen Dreams";
    public static final String BOREHOLE_PLANETS_OLD_CRATER = "Borehole Planet's Old Crater";
    
    // ======================== ELITE ========================
    
    public static final String GEPARD = "Gepard";
    public static final String BRONYA = "Bronya";
    public static final String SVAROG = "Svarog";
    
    public static final String HOOLAY = "Hoolay";
    public static final String ABUNDANT_EBON_DEER = "Abundant Ebon Deer";
    public static final String CIRRUS = "Cirrus";
    public static final String YANQING = "Yanqing";
    
    public static final String SOMETHING_UNTO_DEATH = "Something Unto Death";
    public static final String AVENTURINE_OF_STRATAGEMS = "Aventurine of Stratagems";
    public static final String SAM = "Sam";
    public static final String THE_PAST_PRESENT_AND_ETERNAL_SHOW = "The Past, Present, and Eternal Show";
    public static final String BLAZNANA_MONKEY_TRICK = "Blaznana Monkey Trick";
    
    // ======================== STRONGER ======================
    
    public static final String GRIZZLY = "Grizzly";
    public static final String FRIGID_PROWLER = "Frigid Prowler";
    public static final String GUARDIAN_SHADOW = "Guardian Shadow";
    public static final String DECAYING_SHADOW = "Decaying Shadow";
    public static final String SILVERMANE_LIEUTENANT = "Silvermane Lieutenant";
    public static final String VAGRANTS = "Vagrants";
    public static final String DIREWOLF = "Direwolf";
    public static final String STORMBRINGER = "Stormbringer";

    public static final String SHAPE_SHIFTER = "Shape Shifter";
    public static final String AUROMATON_GATEKEEPER = "Auromaton Gatekeeper";
    public static final String HOWLING_CASKET = "Howling Casket";
    public static final String AURUMATON_SPECTRAL_ENVOY = "Aurumaton Spectral Envoy";
    public static final String MALEFIC_APE = "Malefic Ape";
    public static final String THE_ASCENDED = "The Ascended";
    public static final String TWIGS = "Twigs";
    
    public static final String SWEET_GORILLA = "Sweet Gorilla";
    public static final String BEYOND_OVERCOOKED = "Beyond Overcooked";
    public static final String SHELL_OF_FADED_RAGE = "ShellOfFadedRage";
    public static final String PAST = "Past Confined and Caged";
    public static final String PRESENT = "Present Inebriated in Revelry";
    public static final String TOMORROW = "Tomorrow in Harmonious Chords";
    public static final String COMPANY = "Team Leader";
    
    // ======================== NORMAL ========================
    
    public static final String TWO_AUTOMATONS = "2 Automatons";
    public static final String SHADEWALKERS = "Shadewalkers";
    public static final String MASK_N_SPAWNS = "Mask and Spawn";
    public static final String VAGRANT = "Vagrant";
    
    public static final String DRAGONFISH_N_FLOATINGS = "Dragonfish and Dracolion";
    public static final String TWO_MARA_STRUCK = "Mara Struck Soldier and Internal Alchemist";
    public static final String HOUNDS = "Wooden Lupus and Golden Hound";
    public static final String CLOUD_KNIGHTS_PATROLLERS = "2 Cloud Knights Patroller";
    
    public static final String HOUND_N_DOMESCREEN = "Hound and Domescreen";
    public static final String TWO_FLOATINGS_N_HEARTBREAKER = "2 Floatings and Heartbreaker";
    public static final String MEMORY_ZONE_MEME = "2 Insatiable Vanity and 2 Something In The Mirror";
    
    
    public static BaseMonster getRandomAutomaton(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(2)) {
            case 0:
                result = new Beetle(x, y);
                break;
            case 1:
                result = new Spider(x, y);
                break;
            case 2:
                result = new Hound(x, y);
                break;
        }
        return result;
    }
    
    public static BaseMonster getRandomSpawn(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(2)) {
            case 0:
                result = new Windspawn(x, y);
                break;
            case 1:
                result = new Thunderspawn(x, y);
                break;
            case 2:
                result = new Flamespawn(x, y);
                break;
        }
        return result;
    }
    
    public static BaseMonster getRandomMaraStruck(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(3)) {
            case 0:
                result = new MaraStruckSoldier(x, y);
                break;
            case 1:
                result = new InternalAlchemist(x, y);
                break;
            case 2:
                result = new MaraStruckWarden(x, y);
                break;
            case 3:
                result = new Ballistarius(x, y);
                break;
        }
        return result;
    }
    
    public static BaseMonster getRandomFloating(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(1)) {
            case 0:
                result = new ObedientDracolion(x, y);
                break;
            case 1:
                result = new GoldenCloudToad(x, y);
                break;
        }
        return result;
    }
    
    public static BaseMonster getRandomDreamjoltTroupe(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(1)) {
            case 0:
                result = new MrDomescreen(x, y);
                break;
            case 1:
                result = new BubbleHound(x, y);
                break;
        }
        return result;
    }
    
    public static BaseMonster getRandomFloatingPenacony(float x, float y) {
        BaseMonster result = null;
        switch (AbstractDungeon.monsterRng.random(1)) {
            case 0:
                result = new SpringLoader(x, y);
                break;
            case 1:
                result = new Birdskull(x, y);
                break;
            case 2:
                result = new Allseer(x, y);
                break;
        }
        return result;
    }
}
