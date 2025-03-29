package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardSelectManager;

public class JellyfishOnTheStaircase extends BaseRelic {
    public static final String ID = JellyfishOnTheStaircase.class.getSimpleName();
    
    public JellyfishOnTheStaircase() {
        super(ID);
    }

    public void onEquip() {
        CardSelectManager.getInstance().addEvent(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, this.DESCRIPTIONS[1], false, CardSelectManager.UsagePreset.PURGE);
    }
}
