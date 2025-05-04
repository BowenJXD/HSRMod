package hsrmod.relics.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.MoveCardsAction;
import hsrmod.cards.BaseCard;
import hsrmod.relics.BaseRelic;

import java.util.function.Predicate;

public class KnightOfPurityPalace extends BaseRelic {
    public static final String ID = KnightOfPurityPalace.class.getSimpleName();

    public KnightOfPurityPalace() {
        super(ID);
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStart();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.hasTag(BaseCard.CardTags.STARTER_DEFEND)) {
                c.isInnate = true;
                c.baseBlock += 2;
                c.applyPowers();
            }
        }
    }

    @Override
    public void onShuffle() {
        super.onShuffle();
        addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.isInnate;
            }
        }, BaseMod.MAX_HAND_SIZE));
        addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.isInnate;
            }
        }, BaseMod.MAX_HAND_SIZE));
        // addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.exhaustPile, (c) -> c.isInnate, 99));
    }
}
