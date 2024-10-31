package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.utils.ModHelper;

public class NightBeyondPyre extends BaseCard {
    public static final String ID = NightBeyondPyre.class.getSimpleName();
    
    public NightBeyondPyre() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(BreakEfficiencyPower.POWER_ID))
            addToBot(new ApplyPowerAction(p, p, new BreakEfficiencyPower(p, magicNumber), magicNumber));
        ModHelper.addToBotAbstract(() -> {
            if (p.hand.isEmpty())
                addToBot(new DrawCardAction(magicNumber));
        });
    }
}
