package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Robin2 extends BaseCard {
    public static final String ID = Robin2.class.getSimpleName();
    
    public Robin2() {
        super(ID);
        energyCost = 140;
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int amount = energyOnUse + magicNumber;

        for (AbstractCard card : p.hand.group) {
            if (card.hasTag(FOLLOW_UP)) {
                card.setCostForTurn(0);
            }
            amount--;
            if (amount == 0) break;
        }

        amount = energyOnUse + magicNumber;
        
        for (AbstractCard card : p.hand.group) {
            if (card.hasTag(FOLLOW_UP)) {
                addToBot(new FollowUpAction(card));
            }
            amount--;
            if (amount == 0) break;
        }
        
        p.energy.use(EnergyPanel.totalCount);
    }
}
