package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GoldCoinOfDiscord extends BaseRelic {
    public static final String ID = GoldCoinOfDiscord.class.getSimpleName();

    public GoldCoinOfDiscord() {
        super(ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        flash();
        int amt = AbstractDungeon.player.gold * magicNumber / 100;
        if (amt > 0) {
            AbstractDungeon.player.gainGold(amt);
        }
    }
}
