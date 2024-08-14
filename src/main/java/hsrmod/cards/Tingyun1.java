package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.powers.EnergyCounter;

public class Tingyun1 extends BaseCard {
    public static final String ID = Tingyun1.class.getSimpleName();

    public Tingyun1() {
        super(ID);
        energyCost = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!checkEnergy()) return;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new EnergyCounter(p, magicNumber),
                        magicNumber
                )
        );
    }
}
