package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;

import java.util.Iterator;

public class Aggregator extends BaseCard {
    public static final String ID = Aggregator.class.getSimpleName();
    
    public Aggregator() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        int count = p.hand.group.stream().filter((c) ->  c instanceof BaseCard && ( (BaseCard)c).energyCost > 0 ).mapToInt((c) -> 1).sum();
        
        addToBot(new DrawCardAction(p, count));
        addToBot(new GainEnergyAction(count));
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, count * magicNumber), count * magicNumber));
    }
}
