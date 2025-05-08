package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
        addToBot(new ReducePowerAction(m, p, ToughnessPower.POWER_ID, amt));
        if (mToughness != null 
                && ModHelper.getPowerCount(p, ToughnessPower.POWER_ID) < mToughness.amount) {
            addToBot(new ApplyPowerAction(p, p, new ToughnessPower(p, amt), amt));
        }
    }
}
