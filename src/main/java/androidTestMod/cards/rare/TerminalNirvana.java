package androidTestMod.cards.rare;

import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class TerminalNirvana extends BaseCard {
    public static final String ID = TerminalNirvana.class.getSimpleName();
    
    int magicNumberCache = -1;
    
    public TerminalNirvana() {
        super(ID);
        exhaust = true;
        selfRetain = true;
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
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
        
        if (upgraded) {
            upgradeMagicNumber(magicNumber);
            setBaseEnergyCost(getEnergyCost() * 2);
        }
    }
}
