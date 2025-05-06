package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

public class FaithBond extends BaseRelic {
    public static final String ID = FaithBond.class.getSimpleName();
    
    public static final int DISCOUNT = 33; // %

    public FaithBond() {
        super(ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (room instanceof ShopRoom) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }
}
