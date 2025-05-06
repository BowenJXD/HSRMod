package androidTestMod.cardsV2.Erudition;

import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.SMR2AmygdalaPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SMR2Amygdala extends BaseCard {
    public static final String ID = SMR2Amygdala.class.getSimpleName();

    public SMR2Amygdala() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SMR2AmygdalaPower(magicNumber)));
    }
}
