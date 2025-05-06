package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
