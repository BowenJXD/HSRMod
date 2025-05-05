package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfTheHunt extends WaxRelic {
    public static final String ID = WaxOfTheHunt.class.getSimpleName();

    public WaxOfTheHunt() {
        super(ID, CustomEnums.THE_HUNT);
    }
}
