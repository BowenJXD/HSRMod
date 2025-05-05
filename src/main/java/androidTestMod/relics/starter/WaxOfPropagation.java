package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfPropagation extends WaxRelic {
    public static final String ID = WaxOfPropagation.class.getSimpleName();

    public WaxOfPropagation() {
        super(ID, CustomEnums.PROPAGATION);
    }
}
