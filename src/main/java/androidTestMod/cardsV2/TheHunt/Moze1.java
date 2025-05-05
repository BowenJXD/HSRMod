package androidTestMod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueDebuffs.PreyPower;
import androidTestMod.utils.ModHelper;

public class Moze1 extends BaseCard {
    public static final String ID = Moze1.class.getSimpleName();
    
    public Moze1(){
        super(ID);
        cardsToPreview = new Moze2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new PreyPower(m, magicNumber), magicNumber - ModHelper.getPowerCount(m, PreyPower.POWER_ID)));
    }
}
