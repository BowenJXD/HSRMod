package hsrmod.cardsV2.Paths;

import hsrmod.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TheHunt extends BaseCard {
    public static final String ID = TheHunt.class.getSimpleName();
    
    public TheHunt() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
