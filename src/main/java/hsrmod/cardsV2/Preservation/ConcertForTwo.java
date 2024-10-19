package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class ConcertForTwo extends BaseCard {
    public static final String ID = ConcertForTwo.class.getSimpleName();
    
    public ConcertForTwo() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.getMonsters().monsters.stream().filter(q -> !q.isDeadOrEscaped() && q.currentHealth > 0).forEach(q -> {
            addToBot(new GainBlockAction(p, p, block));
            addToBot(new GainBlockAction(q, baseBlock));
        });
        if (upgraded) addToBot(new GainBlockAction(p, p, block));
    }
}
