package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.uniqueDebuffs.ThanatoplumRebloomPower;
import hsrmod.utils.ModHelper;

public class RuanMei1 extends BaseCard {
    public static final String ID = RuanMei1.class.getSimpleName();

    public RuanMei1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BreakEfficiencyPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
    }
}
