package androidTestMod.cardsV2.Erudition;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.SilverheartGuardsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
