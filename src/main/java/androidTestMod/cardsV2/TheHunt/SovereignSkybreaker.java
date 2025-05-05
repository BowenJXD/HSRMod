package androidTestMod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.SovereignSkybreakerPower;

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
