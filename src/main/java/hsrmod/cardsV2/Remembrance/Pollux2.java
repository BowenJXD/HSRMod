package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AddToDarkAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Pollux2 extends BaseCard {
    public static final String ID = Pollux2.class.getSimpleName();

    public Pollux2() {
        super(ID);
        exhaust = true;
        isMultiDamage = true;
        tags.add(CustomEnums.TERRITORY);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new AddToDarkAction(p.orbs, magicNumber));
        
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.FIRE));
    }
}
