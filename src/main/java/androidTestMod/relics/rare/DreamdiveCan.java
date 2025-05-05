package androidTestMod.relics.rare;

import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.relics.BaseRelic;

public class DreamdiveCan extends BaseRelic {
    public static final String ID = DreamdiveCan.class.getSimpleName();

    public DreamdiveCan() {
        super(ID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        flash();
        addToBot(new DiscardPileToTopOfDeckAction(AbstractDungeon.player));
    }
}
