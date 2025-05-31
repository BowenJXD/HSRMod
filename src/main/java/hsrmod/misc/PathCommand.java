package hsrmod.misc;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Prefs;
import hsrmod.characters.StellaCharacter;
import hsrmod.patches.PathSelectScreen;

public class PathCommand extends ConsoleCommand {
    public PathCommand() {
        this.followup.put("lock", PathLockCommand.class);
        this.followup.put("unlock", PathUnlockCommand.class);
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] strings, int i) {
        cmdHelp();
    }

    @Override
    public void errorMsg() {
        cmdHelp();
    }

    public static void cmdHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* lock");
        DevConsole.log("* unlock");
    }

    public static class PathLockCommand extends ConsoleCommand {
        public PathLockCommand() {
            this.minExtraTokens = 1;
            this.maxExtraTokens = 1;
            this.simpleCheck = true;
        }

        @Override
        protected void execute(String[] strings, int i) {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            stellaPref.putInteger("WIN_COUNT", 0);
            PathSelectScreen.Inst.setPathUnlocked();
            DevConsole.log("Paths are now locked to 3.");
        }
    }

    public static class PathUnlockCommand extends ConsoleCommand {
        public PathUnlockCommand() {
            this.minExtraTokens = 1;
            this.maxExtraTokens = 1;
            this.simpleCheck = true;
        }

        @Override
        protected void execute(String[] strings, int i) {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            stellaPref.putInteger("WIN_COUNT", PathSelectScreen.PATHS.size());
            PathSelectScreen.Inst.setPathUnlocked();
            DevConsole.log("Paths are now all unlocked.");
        }
    }
}
