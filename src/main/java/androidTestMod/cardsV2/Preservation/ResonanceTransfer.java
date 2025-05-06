package androidTestMod.cardsV2.Preservation;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.ResonanceTransferPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ResonanceTransfer extends BaseCard {
    public static final String ID = ResonanceTransfer.class.getSimpleName();

    public ResonanceTransfer() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ResonanceTransferPower(upgraded)));
    }
}
