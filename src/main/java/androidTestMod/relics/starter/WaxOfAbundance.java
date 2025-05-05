package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfAbundance extends WaxRelic {
    public static final String ID = WaxOfAbundance.class.getSimpleName();

    public WaxOfAbundance() {
        super(ID, CustomEnums.ABUNDANCE);
    }
}
