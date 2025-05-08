package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.RandomCardFromDrawPileToHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;

public class Tingyun1 extends BaseCard {
    public static final String ID = Tingyun1.class.getSimpleName();

    public Tingyun1() {
        super(ID);
        setBaseEnergyCost(10);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
        if (upgraded) addToTop(new RandomCardFromDrawPileToHandAction());
    }
}
