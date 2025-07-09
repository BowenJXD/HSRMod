package hsrmod.custommods;

import com.megacrit.cardcrawl.screens.custom.CustomMod;
import hsrmod.modcore.HSRMod;

import java.util.function.Function;

public class InfoMod extends CustomMod {
    public static final String ID = HSRMod.makePath(InfoMod.class.getSimpleName());

    String rawDescription;
    Function<String, String> descProcessor = null;

    public InfoMod(String postfix, Function<String, String> descProcessor) {
        super(ID + postfix, "r", false);
        this.descProcessor = descProcessor;
        rawDescription = description;
    }

    public InfoMod(String postfix) {
        this(postfix, null);
    }

    @Override
    public void update(float y) {
        super.update(y);
        selected = false;
        if (descProcessor != null) {
            description = descProcessor.apply(rawDescription);
        }
    }
}
