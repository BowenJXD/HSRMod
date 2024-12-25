package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.Trailblazer8Power;

public class Trailblazer8 extends BaseCard {
    public static final String ID = Trailblazer8.class.getSimpleName();

    public Trailblazer8() {
        super(ID);        
        cardsToPreview = new Quake();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new Trailblazer8Power(magicNumber)));
    }
}
