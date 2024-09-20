package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.DivinityPower;

public class Macrosegregation extends BaseCard {
    public static final String ID = Macrosegregation.class.getSimpleName();
    
    public Macrosegregation() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) 
            addToBot(new RemoveSpecificPowerAction(p, p, DexterityPower.POWER_ID));
        
        addToBot(new ApplyPowerAction(p, p, new DivinityPower(1f, magicNumber / 100f)));
    }
}
