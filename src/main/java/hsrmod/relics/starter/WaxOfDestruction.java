package hsrmod.relics.starter;

import hsrmod.modcore.CustomEnums;

public class WaxOfDestruction extends WaxRelic {
    public static final String ID = WaxOfDestruction.class.getSimpleName();

    public WaxOfDestruction(){
        super(ID, CustomEnums.DESTRUCTION);
    }
}
