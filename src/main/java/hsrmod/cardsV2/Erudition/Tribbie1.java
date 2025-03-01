package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.TribbiePower;

public class Tribbie1 extends BaseCard {
    public static final String ID = Tribbie1.class.getSimpleName();
    
    public Tribbie1() {
        super(ID);
        energyCost = 120;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TribbiePower(magicNumber, upgraded)));
    }
}
