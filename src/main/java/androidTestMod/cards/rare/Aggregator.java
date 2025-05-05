package androidTestMod.cards.rare;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.misc.EnergyPower;

public class Aggregator extends BaseCard {
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
        int count = 0;
        for (AbstractCard c : p.hand.group) {
            if (c.hasTag(CustomEnums.ENERGY_COSTING) && c != Aggregator.this) {
                int i = 1;
                count += i;
            }
        }

        addToBot(new DrawCardAction(p, count));
        addToBot(new GainEnergyAction(count));
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, count * magicNumber), count * 40));

        if (upgraded) {
            upgradeMagicNumber(magicNumber);
            setBaseEnergyCost(getEnergyCost() * 2);
        }
    }
}
