package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;

public class StarcrusherWorker extends BaseCard {
    public static final String ID = StarcrusherWorker.class.getSimpleName();
    
    public StarcrusherWorker() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int amt = magicNumber;
        AbstractPower mToughness = m.getPower(ToughnessPower.POWER_ID);
        if (mToughness != null
                && mToughness.amount <= magicNumber
                && mToughness.amount > 0) {
            amt = mToughness.amount - 1;
        }
        addToBot(new ApplyPowerAction(m, p, new ToughnessPower(m, -amt), -amt));
        if (mToughness != null 
                && (p.hasPower(ToughnessPower.POWER_ID) ? p.getPower(ToughnessPower.POWER_ID).amount : 0) < mToughness.amount) {
            addToBot(new ApplyPowerAction(p, p, new ToughnessPower(p, amt), amt));
        }
    }
}
