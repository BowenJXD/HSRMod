package hsrmod.relics.shop;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.relics.starter.PomPomBlessing;

public class WrittenInWater extends BaseRelic {
    public static final String ID = WrittenInWater.class.getSimpleName();

    public WrittenInWater() {
        super(ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        AbstractDungeon.player.heal(magicNumber);
        AbstractRelic pomPomBlessing = AbstractDungeon.player.getRelic(PomPomBlessing.ID);
        if (pomPomBlessing != null) {
            pomPomBlessing.setCounter(pomPomBlessing.counter + 30);
        }
    }
}
