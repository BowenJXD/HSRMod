package hsrmod.relics.starter;

import hsrmod.modcore.CustomEnums;
import hsrmod.relics.BaseRelic;

public class WaxOfPropagation extends WaxRelic {
    public static final String ID = WaxOfPropagation.class.getSimpleName();

    public WaxOfPropagation() {
        super(ID, CustomEnums.PROPAGATION);
    }
}
