package hsrmod.signature.devcommands;

import basemod.devcommands.ConsoleCommand;
import hsrmod.signature.utils.internal.ConfigHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class SignatureDebug extends ConsoleCommand {
    
    public SignatureDebug() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }
    
    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[2].equals("true")) {
            ConfigHelper.setEnableDebugging(true);
        } else if (tokens[2].equals("false")) {
            ConfigHelper.setEnableDebugging(false);
        } else {
            errorMsg();
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        return new ArrayList<>(Arrays.asList("true", "false"));
    }
}
