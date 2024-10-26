package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Gepard1 extends BaseCard {
    public static final String ID = Gepard1.class.getSimpleName();
    
    public Gepard1() {
        super(ID);
        energyCost = 100;
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }
}
