package hsrmod.cards.rare;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.SubscriptionManager;

public class Aggregator extends BaseCard implements PostBattleSubscriber {
    public static final String ID = Aggregator.class.getSimpleName();
    
    int magicNumberCache = -1;
    
    public Aggregator() {
        super(ID);
        exhaust = true;
        setBaseEnergyCost(magicNumber);
        magicNumberCache = magicNumber;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
        setBaseEnergyCost(magicNumber);
        magicNumberCache = magicNumber;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        int count = p.hand.group.stream().filter((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && c != this).mapToInt((c) -> 1).sum();
        
        addToBot(new DrawCardAction(p, count));
        addToBot(new GainEnergyAction(count));
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, count * magicNumber), count * 40));

        if (upgraded) {
            upgradeMagicNumber(magicNumber);
            setBaseEnergyCost(getEnergyCost() * 2);
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (SubscriptionManager.checkSubscriber(this)) {
            setBaseEnergyCost(magicNumberCache);
            upgradeMagicNumber(magicNumberCache - magicNumber);
        }
    }
}
