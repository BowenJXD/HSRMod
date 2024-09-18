package hsrmod.relics.common;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.relics.BaseRelic;

public class PriceOfPeace extends BaseRelic {
    public static final String ID = PriceOfPeace.class.getSimpleName();

    public PriceOfPeace() {
        super(ID);
    }

    public void justEnteredRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.gainGold(magicNumber);
        }
    }
}
