package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.SilverheartGuardsPower;

public class SilverheartGuards extends BaseCard {
    public static final String ID = SilverheartGuards.class.getSimpleName();
    
    public SilverheartGuards() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SilverheartGuardsPower(1, magicNumber)));
    }
}
