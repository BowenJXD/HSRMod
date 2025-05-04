package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class March7th5 extends BaseCard {
    public static final String ID = March7th5.class.getSimpleName();
    
    public March7th5(){
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.canUpgrade() && c != this) {
                addToBot(new UpgradeSpecificCardAction(c));
                count++;
            }
        }
    }
}
