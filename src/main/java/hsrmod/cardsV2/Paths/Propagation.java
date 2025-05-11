package hsrmod.cardsV2.Paths;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

@AutoAdd.Ignore
public class Propagation extends BaseCard {
    public static final String ID = Propagation.class.getSimpleName();
    
    public Propagation() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
