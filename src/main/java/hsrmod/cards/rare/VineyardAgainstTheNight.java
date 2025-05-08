package hsrmod.cards.rare;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.AftertastePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VineyardAgainstTheNight extends BaseCard {
    public static final String ID = VineyardAgainstTheNight.class.getSimpleName();
    
    public VineyardAgainstTheNight() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AftertastePower(p, magicNumber)));
    }
}
