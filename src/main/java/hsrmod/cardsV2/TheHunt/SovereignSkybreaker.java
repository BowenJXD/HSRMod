package hsrmod.cardsV2.TheHunt;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.SovereignSkybreakerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SovereignSkybreaker extends BaseCard {
    public static final String ID = SovereignSkybreaker.class.getSimpleName();
    
    public SovereignSkybreaker(){
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SovereignSkybreakerPower(magicNumber, upgraded), 0));
    }
}
