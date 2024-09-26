package hsrmod.obsoleteCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AfterImagePower;
import hsrmod.cards.BaseCard;

public class ConcertForTwo extends BaseCard {
    public static final String ID = ConcertForTwo.class.getSimpleName();
    
    public ConcertForTwo() {
        super(ID);
        exhaust = true;
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AfterImagePower(p, magicNumber), magicNumber));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, baseBlock));
    }
}
