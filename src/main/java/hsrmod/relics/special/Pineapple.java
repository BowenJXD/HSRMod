package hsrmod.relics.special;

import hsrmod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Pineapple extends BaseRelic {
    public static final String ID = Pineapple.class.getSimpleName();
    
    public Pineapple() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (usedUp) return;
        flash();
        int dmg = AbstractDungeon.player.currentHealth * 15 / 100;
        AbstractDungeon.player.damage(new DamageInfo(null, dmg, DamageInfo.DamageType.HP_LOSS));
        reduceCounterAndCheckDestroy();
    }
}
