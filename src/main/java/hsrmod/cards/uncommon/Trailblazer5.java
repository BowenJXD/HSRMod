package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.Trailblazer5Power;

public class Trailblazer5 extends BaseCard {
    public static final String ID = Trailblazer5.class.getSimpleName();

    public Trailblazer5() {
        super(ID);
        energyCost = 140;
        selfRetain = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        // addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new Trailblazer5Power(p, 1), 1));
    }
}
