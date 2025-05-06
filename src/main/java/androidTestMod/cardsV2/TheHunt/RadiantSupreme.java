package androidTestMod.cardsV2.TheHunt;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.RadiantSupremePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
