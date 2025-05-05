package androidTestMod.relics.starter;

import androidTestMod.modcore.CustomEnums;

public class WaxOfElation extends WaxRelic {
    public static final String ID = WaxOfElation.class.getSimpleName();
    
    public WaxOfElation(){
        super(ID, CustomEnums.ELATION);
    }
}
