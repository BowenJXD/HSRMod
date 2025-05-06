package androidTestMod.cardsV2.Paths;

import androidTestMod.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Nihility extends BaseCard {
    public static final String ID = Nihility.class.getSimpleName();
    
    public Nihility() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
