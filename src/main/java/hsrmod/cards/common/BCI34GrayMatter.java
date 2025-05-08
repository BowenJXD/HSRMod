package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BrainInAVatPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BCI34GrayMatter extends BaseCard {
    public static final String ID = BCI34GrayMatter.class.getSimpleName();
    
    public BCI34GrayMatter() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BrainInAVatPower(p, magicNumber), magicNumber));
    }
}
