package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;

public class Tingyun1 extends BaseCard {
    public static final String ID = Tingyun1.class.getSimpleName();

    public Tingyun1() {
        super(ID);
        energyCost = 10;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ApplyPowerAction(
                        p,
                        p,
                        new EnergyPower(p, magicNumber),
                        magicNumber
                )
        );
        addToBot(
                new ApplyPowerAction(
                        p,
                        p,
                        new StrengthPower(p, 1),
                        1
                )
        );
        if (upgraded)
            addToBot(new DrawCardAction(1));
    }
}
