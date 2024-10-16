package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;

public class Amber extends BaseCard {
    public static final String ID = Amber.class.getSimpleName();
    
    public Amber() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.currentBlock == 0)
            addToBot(new GainBlockAction(p, p, block));
        
        addToBot(new AOEAction((q) -> {
            if (q.currentBlock == 0)
                return new GainBlockAction(q, magicNumber);
            return null;
        }));
    }
}
