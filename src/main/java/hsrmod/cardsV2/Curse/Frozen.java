package hsrmod.cardsV2.Curse;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class Frozen extends BaseCard {
    public static final String ID = Frozen.class.getSimpleName();
    
    public Frozen() {
        super(ID, CardColor.COLORLESS);
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }
}
