package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class AmbergrisCheese extends BaseRelic {
    public static final String ID = AmbergrisCheese.class.getSimpleName();
    
    public AmbergrisCheese() {
        super(ID);
    }

    @Override
    public void onVictory() {
        flash();
        AbstractDungeon.player.heal(magicNumber);
    }
}
