package hsrmod.obsoleteCards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import hsrmod.cards.BaseCard;

public class DestinysThreadsForewoven extends BaseCard {
    public static final String ID = DestinysThreadsForewoven.class.getSimpleName();
    
    public DestinysThreadsForewoven() {
        super(ID);
        isEthereal = true;
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isEthereal = false;
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, baseBlock));
    }
}
