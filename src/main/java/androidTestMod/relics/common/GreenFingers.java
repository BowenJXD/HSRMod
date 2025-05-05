package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RelicEventHelper;

public class GreenFingers extends BaseRelic {
    public static final String ID = GreenFingers.class.getSimpleName();

    public GreenFingers() {
        super(ID);
        setCounter(magicNumber);
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        if (usedUp) return;
        setCounter(counter - 1);
        int goldAmt = 0;
        switch (counter) {
            case 6:
                goldAmt = 50;
                break;
            case 3:
                goldAmt = 100;
                break;
            case 0:
                goldAmt = 150;
                destroy();
                break;
        }
        if (goldAmt > 0) {
            flash();
            RelicEventHelper.gainGold(goldAmt);
        }
    }
}
