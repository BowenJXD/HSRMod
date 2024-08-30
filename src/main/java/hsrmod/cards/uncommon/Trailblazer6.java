package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.only.Trailblazer5Power;
import hsrmod.powers.only.Trailblazer6Power;

public class Trailblazer6 extends BaseCard {
    public static final String ID = Trailblazer6.class.getSimpleName();

    public Trailblazer6() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(Trailblazer5Power.POWER_ID))
            addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber * 2), magicNumber * 2));
        else {
            addToBot(new MakeTempCardInHandAction(new Trailblazer5()));
            addToBot(new ApplyPowerAction(p, p, new Trailblazer5Power(p, 1), 1));
        }
    }
}
