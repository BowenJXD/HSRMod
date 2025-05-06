package androidTestMod.relics.uncommon;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.CardSelectManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class JellyfishOnTheStaircase extends BaseRelic {
    public static final String ID = JellyfishOnTheStaircase.class.getSimpleName();
    
    public JellyfishOnTheStaircase() {
        super(ID);
    }

    public void onEquip() {
        CardSelectManager.getInstance().addEvent(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, this.DESCRIPTIONS[1], false, CardSelectManager.UsagePreset.PURGE);
    }
}
