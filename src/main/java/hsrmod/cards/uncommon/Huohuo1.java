package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.HuohuoPower;

public class Huohuo1 extends BaseCard {
    public static final String ID = Huohuo1.class.getSimpleName();
    
    public Huohuo1() {
        super(ID);
        exhaust = true;
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        AbstractPower power = p.getPower(HuohuoPower.POWER_ID);
        if (power != null)
            ((HuohuoPower) power).upgraded = upgraded;
        else
            addToBot(new ApplyPowerAction(p, p, new HuohuoPower(p, upgraded)));
    }
}
