package androidTestMod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.relics.BaseRelic;

public class IllusoryAutomaton extends BaseRelic {
    public static final String ID = IllusoryAutomaton.class.getSimpleName();

    public IllusoryAutomaton() {
        super(ID);
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }
}
