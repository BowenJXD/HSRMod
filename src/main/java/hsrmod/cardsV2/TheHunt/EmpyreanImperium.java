package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.EmpyreanImperiumPower;

public class EmpyreanImperium extends BaseCard {
    public static final String ID = EmpyreanImperium.class.getSimpleName();
    
    public EmpyreanImperium(){
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EmpyreanImperiumPower(upgraded, magicNumber), 0));
    }
}
