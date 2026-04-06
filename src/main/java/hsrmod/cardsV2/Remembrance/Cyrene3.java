package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.RecollectionPower;

public class Cyrene3 extends BaseCard {
    public static final String ID = Cyrene3.class.getSimpleName();

    public Cyrene3() {
        super(ID);
        selfRetain = true;
        cardsToPreview = new Cyrene4();
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RecollectionPower(p, magicNumber)));
    }
}
