package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfDestruction extends WaxRelic {
    public static final String ID = WaxOfDestruction.class.getSimpleName();

    public WaxOfDestruction(){
        super(ID, CustomEnums.DESTRUCTION);
    }
}
