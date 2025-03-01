/*
package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.TopazNumbyPower;

public class TopazNumby1 extends BaseCard {
    public static final String ID = TopazNumby1.class.getSimpleName();
    
    public TopazNumby1() {
        super(ID);
        cardsToPreview = new TopazNumby2();
    }

    @Override
    public void upgrade() {
        cardsToPreview.upgrade();
        super.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TopazNumbyPower(1, upgraded)));
    }
}
*/
