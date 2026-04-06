package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Castorice3 extends BaseCard {
    public static final String ID = Castorice3.class.getSimpleName();

    public Castorice3() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
