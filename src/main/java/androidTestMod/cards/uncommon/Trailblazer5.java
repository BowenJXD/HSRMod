package androidTestMod.cards.uncommon;

import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.uniqueBuffs.Trailblazer5Power;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Trailblazer5 extends BaseCard {
    public static final String ID = Trailblazer5.class.getSimpleName();

    public Trailblazer5() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        // addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new Trailblazer5Power()));
    }
}
