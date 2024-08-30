package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

public class SilverWolf1 extends BaseCard {
    public static final String ID = SilverWolf1.class.getSimpleName();
    
    public SilverWolf1() {
        super(ID);
        energyCost = 110;
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage), ElementType.Quantum, 4));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
        if (upgraded)
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
    }
}
