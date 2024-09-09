package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.cards.BaseCard;

public class FuXuan1 extends BaseCard {
    public static final String ID = FuXuan1.class.getSimpleName();
    
    public FuXuan1() {
        super(ID);
        exhaust = true;
        
        energyCost = 130;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int heal = (p.maxHealth - p.currentHealth) / 10;
        addToBot(new HealAction(p, p, heal));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
    }
}
