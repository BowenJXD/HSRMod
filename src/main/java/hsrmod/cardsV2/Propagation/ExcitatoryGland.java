package hsrmod.cardsV2.Propagation;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ExcitatoryGlandPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ExcitatoryGland extends BaseCard {
    public static final String ID = ExcitatoryGland.class.getSimpleName();
    
    public ExcitatoryGland() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ExcitatoryGlandPower()));
    }
}
