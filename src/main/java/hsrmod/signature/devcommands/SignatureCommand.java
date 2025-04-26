package hsrmod.signature.devcommands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class SignatureCommand extends ConsoleCommand {
    public SignatureCommand() {
        this.followup.put("unlock", SignatureUnlock.class);
        this.followup.put("lock", SignatureLock.class);
        this.followup.put("enable", SignatureEnable.class);
        this.followup.put("disable", SignatureDisable.class);
        this.followup.put("debug", SignatureDebug.class);
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        cmdHelp();
    }

    public void errorMsg() {
        cmdHelp();
    }

    public static void cmdHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* enable [id/all]");
        DevConsole.log("* disable [id/all]");
        DevConsole.log("* lock [id/all]");
        DevConsole.log("* unlock [id/all]");
        DevConsole.log("* debug [boolean]");
    }
}
