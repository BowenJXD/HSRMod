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
        BreakEfficiencyPower be = new BreakEfficiencyPower(p, magicNumber);
        if (upgraded) be.minAmount = 1;
        addToBot(new ApplyPowerAction(p, p, be, magicNumber));
        ModHelper.addToBotAbstract(() -> {
            AbstractPower power = p.getPower(BreakEfficiencyPower.POWER_ID);
            if (power != null && upgraded) {
                ((BreakEfficiencyPower) power).minAmount = 1;
                power.updateDescription();
            }
        });
        addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
    }
}
