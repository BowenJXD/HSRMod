package hsrmod.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import hsrmod.utils.PathDefine;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong"
)
public class MusicPatch {
    public MusicPatch() {
    }

    @SpirePrefixPatch
    public static SpireReturn<Music> Prefix(TempMusic t, String key) {
        Music music = null;
        switch (key) {
            case "RobinBGM":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "RobinBGM.mp3");
                break;
            case "Ensemble Cast":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "Ensemble Cast.mp3");
                break;
            case "End of the Eternal Freeze_1":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "Wildfire_1.mp3");
                break;
            case "End of the Eternal Freeze_2":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "Wildfire_2.mp3");
                break;
            case "Divine Seed_1":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "PedujaraEvenImmortalityEnds.mp3");
                break;
            case "Divine Seed_2":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "PedujaraDemiselessExistence.mp3");
                break;
            case "Salutations of Ashen Dreams":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "HopeIsTheThingWithFeathers.mp3");
                break;
            case "Destruction's Beginning":
                music = MainMusic.newMusic(PathDefine.MUSIC_PATH + "DawnOfDisaster.mp3");
                break;
        }
        if (music == null)
            return SpireReturn.Continue();
        else
            return SpireReturn.Return(music);
    }
}
