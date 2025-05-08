package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Yukong1 extends BaseCard {
    public static final String ID = Yukong1.class.getSimpleName();
    
    public Yukong1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int num = Math.min(energyOnUse, p.energy.energy + magicNumber);
        if (num <= 0) return;
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, num), num));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, num), num));
    }
}
