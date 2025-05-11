package hsrmod.cardsV2.Paths;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

@AutoAdd.Ignore
public class Preservation extends BaseCard {
    public static final String ID = Preservation.class.getSimpleName();
    
    public Preservation() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
