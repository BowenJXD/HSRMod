package hsrmod.cards.rare;

import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.AllThingsArePossiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AllThingsArePossible extends BaseCard {
    public static final String ID = AllThingsArePossible.class.getSimpleName();
    
    public AllThingsArePossible() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AllThingsArePossiblePower(upgraded)));
    }
}
