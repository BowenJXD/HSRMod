package hsrmod.cardsV2.Propagation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.SerfOfCalamityPower;

public class SerfOfCalamity extends BaseCard {
    public static final String ID = SerfOfCalamity.class.getSimpleName();
    
    public SerfOfCalamity() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SerfOfCalamityPower(magicNumber)));
    }
}
