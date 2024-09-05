package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.HuohuoPower;

public class Huohuo1 extends BaseCard {
    public static final String ID = Huohuo1.class.getSimpleName();
    
    public Huohuo1() {
        super(ID);
        selfRetain = true;
        exhaust = true;
        energyCost = 140;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        addToBot(new ApplyPowerAction(p, p, new HuohuoPower(p, 1, upgraded), 1));
    }
}
