package hsrmod.relics.starter;

import hsrmod.modcore.CustomEnums;

public class WaxOfPreservation extends WaxRelic {
    public static final String ID = WaxOfPreservation.class.getSimpleName();

    public WaxOfPreservation() {
        super(ID, CustomEnums.PRESERVATION);
    }
}
