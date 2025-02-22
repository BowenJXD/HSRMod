package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.purple.Scrawl;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class MakeTheWorldClamor extends BaseCard {
    public static final String ID = MakeTheWorldClamor.class.getSimpleName();
    
    public MakeTheWorldClamor() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int e = EnergyPower.AMOUNT_LIMIT - ModHelper.getPowerCount(p, EnergyPower.POWER_ID);
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, e), e));
    }
}
