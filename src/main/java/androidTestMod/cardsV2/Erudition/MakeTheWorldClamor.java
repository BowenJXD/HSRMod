package androidTestMod.cardsV2.Erudition;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.misc.EnergyPower;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
