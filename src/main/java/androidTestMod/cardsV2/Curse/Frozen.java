package androidTestMod.cardsV2.Curse;

import androidTestMod.cards.BaseCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Frozen extends BaseCard {
    public static final String ID = Frozen.class.getSimpleName();
    
    public Frozen() {
        super(ID, CardColor.COLORLESS);
        selfRetain = true;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
