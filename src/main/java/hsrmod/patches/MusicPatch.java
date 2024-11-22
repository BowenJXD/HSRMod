package hsrmod.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong"
)
public class MusicPatch {
    public MusicPatch() {
    }

    public static Music Replace(TempMusic t, String key) {
        switch (key) {
            case "SHOP":
                return MainMusic.newMusic("audio/music/STS_Merchant_NewMix_v1.ogg");
            case "SHRINE":
                return MainMusic.newMusic("audio/music/STS_Shrine_NewMix_v1.ogg");
            case "MINDBLOOM":
                return MainMusic.newMusic("audio/music/STS_Boss1MindBloom_v1.ogg");
            case "BOSS_BOTTOM":
                return MainMusic.newMusic("audio/music/STS_Boss1_NewMix_v1.ogg");
            case "BOSS_CITY":
                return MainMusic.newMusic("audio/music/STS_Boss2_NewMix_v1.ogg");
            case "BOSS_BEYOND":
                return MainMusic.newMusic("audio/music/STS_Boss3_NewMix_v1.ogg");
            case "BOSS_ENDING":
                return MainMusic.newMusic("audio/music/STS_Boss4_v6.ogg");
            case "ELITE":
                return MainMusic.newMusic("audio/music/STS_EliteBoss_NewMix_v1.ogg");
            case "CREDITS":
                return MainMusic.newMusic("audio/music/STS_Credits_v5.ogg");
            case "RobinBGM":
                return MainMusic.newMusic("HSRModResources/audio/music/RobinBGM.mp3");
            case "End of the Eternal Freeze_1":
                return MainMusic.newMusic("HSRModResources/audio/music/Wildfire_1.mp3");
            case "End of the Eternal Freeze_2":
                return MainMusic.newMusic("HSRModResources/audio/music/Wildfire_2.mp3");
            case "Divine Seed_1":
                return MainMusic.newMusic("HSRModResources/audio/music/PedujaraEvenImmortalityEnds.mp3");
            case "Divine Seed_2":
                return MainMusic.newMusic("HSRModResources/audio/music/PedujaraDemiselessExistence.mp3");
            case "Salutations of Ashen Dreams":
                return MainMusic.newMusic("HSRModResources/audio/music/HopeIsTheThingWithFeathers.mp3");
            default:
                return MainMusic.newMusic("audio/music/" + key);
        }
    }
}
