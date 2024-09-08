package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import hsrmod.actions.AOEAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DoTPower;

public class OfferingsOfDeception extends BaseCard {
    public static final String ID = OfferingsOfDeception.class.getSimpleName();
    
    public OfferingsOfDeception() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new ApplyPowerAction(m, p, DoTPower.getRandomDoTPower(m, p, 1), 1));
    }
}
