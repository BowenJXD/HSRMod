package hsrmod.relics.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class ChanceJailbreak extends BaseRelic {
    public static final String ID = ChanceJailbreak.class.getSimpleName();

    public ChanceJailbreak() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStart();
        flash();
        addToTop(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, c -> c.isInnate, 10));
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.isInnate) {
            flash();
            addToBot(new DrawCardAction(1));
        }
    }
}
