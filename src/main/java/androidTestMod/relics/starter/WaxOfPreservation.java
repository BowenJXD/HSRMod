package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfPreservation extends WaxRelic {
    public static final String ID = WaxOfPreservation.class.getSimpleName();

    public WaxOfPreservation() {
        super(ID, CustomEnums.PRESERVATION);
    }
}
