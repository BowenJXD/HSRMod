package androidTestMod.relics.rare;

import androidTestMod.actions.MoveCardsAction;
import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Predicate;

public class ParallelUniverseWalkieTalkie extends BaseRelic {
    public static final String ID = ParallelUniverseWalkieTalkie.class.getSimpleName();

    public int returnChance = 20;
    
    public ParallelUniverseWalkieTalkie() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        flash();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (AbstractDungeon.cardRandomRng.random(99) < returnChance 
                && AbstractDungeon.player.hand.size() < 10
                && card.exhaust) {
            flash();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new MoveCardsAction(p.hand, p.exhaustPile, new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard c) {
                    return c == card;
                }
            }));
        }
    }
}
