package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.PowerPower;
import hsrmod.powers.uniqueBuffs.AllThingsArePossiblePower;

public class AllThingsArePossible extends BaseCard {
    public static final String ID = AllThingsArePossible.class.getSimpleName();
    
    public AllThingsArePossible() {
        super(ID);
        isEthereal = true;
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int debuffCount = (int)m.powers.stream().filter(po -> po.type == AbstractPower.PowerType.DEBUFF).count();
        addToBot(new ApplyPowerAction(m, p, new AllThingsArePossiblePower(m, debuffCount), debuffCount));
    }
}
