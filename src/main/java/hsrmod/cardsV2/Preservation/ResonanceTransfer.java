package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ResonanceTransferPower;

public class ResonanceTransfer extends BaseCard {
    public static final String ID = ResonanceTransfer.class.getSimpleName();

    public ResonanceTransfer() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        cardsToPreview = new Quake();
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ResonanceTransferPower(upgraded, magicNumber)));
    }
}
