package hsrmod.cards.rare;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.SubscriptionManager;

public class Aggregator extends BaseCard implements PostBattleSubscriber {
    public static final String ID = Aggregator.class.getSimpleName();
    
    int magicNumberCache = -1;
    
    public Aggregator() {
        super(ID);
        exhaust = true;
        energyCost = magicNumber;
        magicNumberCache = magicNumber;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
        energyCost = magicNumber;
        magicNumberCache = magicNumber;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        int count = p.hand.group.stream().filter((c) ->  c instanceof BaseCard && ( (BaseCard)c).energyCost > 0 ).mapToInt((c) -> 1).sum();
        
        addToBot(new DrawCardAction(p, count));
        addToBot(new GainEnergyAction(count));
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, count * magicNumber), count * magicNumber));

        if (upgraded) {
            upgradeMagicNumber(magicNumber);
            energyCost *= 2;
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (SubscriptionManager.checkSubscriber(this)) {
            energyCost = magicNumberCache;
            upgradeMagicNumber(magicNumberCache - magicNumber);
        }
    }
}
