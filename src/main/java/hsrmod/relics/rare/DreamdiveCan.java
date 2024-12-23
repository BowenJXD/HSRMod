package hsrmod.relics.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

import java.util.List;

public class DreamdiveCan extends BaseRelic {
    public static final String ID = DreamdiveCan.class.getSimpleName();

    public DreamdiveCan() {
        super(ID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        flash();
        if (AbstractDungeon.player.discardPile.isEmpty()) {
            return;
        }
        addToBot(new SelectCardsAction(AbstractDungeon.player.discardPile.group, DESCRIPTIONS[1], list -> {
            if (!list.isEmpty()) {
                addToTop(new MoveCardsAction(AbstractDungeon.player.discardPile, AbstractDungeon.player.drawPile, list::contains, list.size()));
            }
        }));
    }
}
