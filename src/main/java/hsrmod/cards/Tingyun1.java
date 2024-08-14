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

    private static final int ENERGY_COST = 10;
    
    public Tingyun1() {
        super(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = p.getPower(EnergyCounter.POWER_ID);
        if (power == null || power.amount < ENERGY_COST) return;
        power.amount -= ENERGY_COST;
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
