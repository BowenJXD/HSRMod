package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class TheArchitects extends BaseCard {
    public static final String ID = TheArchitects.class.getSimpleName();

    public TheArchitects() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int b = p.hand.size() + (upgraded ? energyOnUse : 0);
        addToBot(new GainBlockAction(p, b));
    }
}
