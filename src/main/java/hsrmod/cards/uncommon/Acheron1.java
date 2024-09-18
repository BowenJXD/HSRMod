package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.SlashedDreamPower;

public class Acheron1 extends BaseCard {
    public static final String ID = Acheron1.class.getSimpleName();
    
    public Acheron1() {
        super(ID);
        isEthereal = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SlashedDreamPower(magicNumber, upgraded), magicNumber));
    }
}
