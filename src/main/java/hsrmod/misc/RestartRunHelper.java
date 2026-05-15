package hsrmod.misc;

import basemod.ReflectionHacks;
import basemod.interfaces.PostRenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame.GameMode;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import java.lang.reflect.Field;

public class RestartRunHelper implements PostRenderSubscriber {
    public static boolean queuedRestart;
    public static boolean queuedScoreRestart;
    public static boolean queuedRoomRestart;
    public static boolean evilMode = false;
    private static Field evilField = null;

    private static RestartRunHelper instance;

    private RestartRunHelper() {
    }

    public static RestartRunHelper getInstance() {
        if (instance == null) {
            instance = new RestartRunHelper();
        }
        return instance;
    }

    public static void restartRun() {
        stopLingeringSounds();
        AbstractDungeon.getCurrRoom().clearEvent();
        /*if (FixAscenscionUnlockOnGameoverWinPatch.updateAscProgress && AbstractDungeon.screen == CurrentScreen.DEATH) {
            ReflectionHacks.privateMethod(DeathScreen.class, "updateAscensionProgress", new Class[0]).invoke(AbstractDungeon.deathScreen, new Object[0]);
        }*/

        if (!queuedRestart) {
            AbstractDungeon.closeCurrentScreen();
        }

        CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen("Exordium");
        AbstractDungeon.reset();
        Settings.hasEmeraldKey = false;
        Settings.hasRubyKey = false;
        Settings.hasSapphireKey = false;
        ShopScreen.resetPurgeCost();
        CardCrawlGame.tips.initialize();
        CardCrawlGame.metricData.clearData();
        CardHelper.clear();
        TipTracker.refresh();
        System.gc();
        if (evilMode) {
            setDownfallMode();
        }

        if (CardCrawlGame.chosenCharacter == null) {
            CardCrawlGame.chosenCharacter = AbstractDungeon.player.chosenClass;
        }

        if (!Settings.seedSet) {
            Long sTime = System.nanoTime();
            Random rng = new Random(sTime);
            Settings.seedSourceTimestamp = sTime;
            Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
            SeedHelper.cachedSeed = null;
        }

        AbstractDungeon.generateSeeds();
        CardCrawlGame.mode = GameMode.CHAR_SELECT;
        queuedRestart = false;
        evilMode = false;
    }

    public static void scoreAndRestart() {
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        restartRun();
        queuedScoreRestart = false;
    }

    public static void restartRoom() {
        stopLingeringSounds();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.reset();
        CardCrawlGame.loadingSave = true;
        CardCrawlGame.mode = GameMode.CHAR_SELECT;
        queuedRoomRestart = false;
    }

    public static void stopLingeringSounds() {
        CardCrawlGame.music.fadeAll();
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.stop("WIND");
        }

        if (AbstractDungeon.scene != null) {
            AbstractDungeon.scene.fadeOutAmbiance();
        }

    }

    public static boolean isDownfallMode() {
        if (evilField == null) {
            try {
                Class<?> clz = Class.forName("downfall.patches.EvilModeCharacterSelect");
                evilField = clz.getField("evilMode");
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                ((ReflectiveOperationException)e).printStackTrace();
            }
        }

        try {
            return evilField.getBoolean((Object)null);
        } catch (IllegalAccessException var1) {
            return false;
        }
    }

    public static void setDownfallMode() {
        if (evilField == null) {
            try {
                Class<?> clz = Class.forName("downfall.patches.EvilModeCharacterSelect");
                evilField = clz.getField("evilMode");
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                ((ReflectiveOperationException)e).printStackTrace();
            }
        }

        try {
            evilField.set((Object)null, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receivePostRender(SpriteBatch spriteBatch) {
        if (queuedScoreRestart) {
            scoreAndRestart();
        } else if (queuedRestart) {
            restartRun();
        } else if (queuedRoomRestart) {
            restartRoom();
        }
    }
}


