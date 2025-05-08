package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.RadiantSupremePower;

public class RadiantSupreme extends BaseCard {
    public static final String ID = RadiantSupreme.class.getSimpleName();
    
    public RadiantSupreme(){
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RadiantSupremePower(magicNumber, upgraded), 0));
    }
}
