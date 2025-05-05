package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfErudition extends WaxRelic {
    public static final String ID = WaxOfErudition.class.getSimpleName();

    public WaxOfErudition() {
        super(ID, CustomEnums.ERUDITION);
    }
}
