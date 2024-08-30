package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.only.ThanatoplumRebloomPower;

public class RuanMei1 extends BaseCard {
    public static final String ID = RuanMei1.class.getSimpleName();

    public RuanMei1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        target = CardTarget.ENEMY;
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p,p, new BreakEfficiencyPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p,p, new BreakEffectPower(p, magicNumber), magicNumber));
        if (upgraded && m != null) addToBot(new ApplyPowerAction(m,p, new ThanatoplumRebloomPower(m, 1), 1));
    }
}
