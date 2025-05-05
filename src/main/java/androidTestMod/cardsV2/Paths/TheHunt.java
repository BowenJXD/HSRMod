package androidTestMod.cardsV2.Paths;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;

public class TheHunt extends BaseCard {
    public static final String ID = TheHunt.class.getSimpleName();
    
    public TheHunt() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
