package androidTestMod.cardsV2.Paths;

import androidTestMod.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Erudition extends BaseCard {
    public static final String ID = Erudition.class.getSimpleName();
    
    public Erudition() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
