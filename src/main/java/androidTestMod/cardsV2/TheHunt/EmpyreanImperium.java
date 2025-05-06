package androidTestMod.cardsV2.TheHunt;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.EmpyreanImperiumPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
