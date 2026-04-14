package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.Trailblazer10Power;

public class Trailblazer10 extends BaseCard {
    public static final String ID = Trailblazer10.class.getSimpleName();

    public Trailblazer10() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new Trailblazer10Power(p, 1)));
    }
}
