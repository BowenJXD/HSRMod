package hsrmod.relics.shop;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TemporaryStake extends BaseRelic {
    public static final String ID = TemporaryStake.class.getSimpleName();

    public TemporaryStake() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        RelicEventHelper.gainGold(300);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (!usedUp) {
            reduceCounterAndCheckDestroy();
            if (usedUp) {
                AbstractDungeon.player.loseGold(450);
            }
        }
    }
}
