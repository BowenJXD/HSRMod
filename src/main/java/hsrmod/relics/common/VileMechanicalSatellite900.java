package hsrmod.relics.common;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

public class VileMechanicalSatellite900 extends BaseRelic {
    public static final String ID = VileMechanicalSatellite900.class.getSimpleName();
    public static final int DISCOUNT = 25; // %

    public VileMechanicalSatellite900() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        trigger(AbstractDungeon.getCurrRoom());
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        trigger(room);
    }
    
    void trigger(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            beginLongPulse();
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    AbstractDungeon.shopScreen.applyDiscount(1 - DISCOUNT / 100f, false);
                }
            });
        } else {
            stopPulse();
        }
    }
}
