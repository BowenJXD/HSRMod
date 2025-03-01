/*
package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.LoadStriatedCortexPower;

public class LoadStriatedCortex extends BaseCard {
    public static final String ID = LoadStriatedCortex.class.getSimpleName();
    
    int[] damageIncrement = new int[]{6, 2, 1};
    
    public LoadStriatedCortex() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        damageIncrement = new int[]{9, 3, 1};
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LoadStriatedCortexPower(damageIncrement)));
    }
}
*/
