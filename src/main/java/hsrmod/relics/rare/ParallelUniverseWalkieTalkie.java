package hsrmod.relics.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class ParallelUniverseWalkieTalkie extends BaseRelic {
    public static final String ID = ParallelUniverseWalkieTalkie.class.getSimpleName();

    public int returnChance = 25;
    
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
        if (AbstractDungeon.cardRandomRng.random(99) < returnChance) {
            flash();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new MoveCardsAction(p.hand, p.exhaustPile, c -> c == card));
        }
    }
}
