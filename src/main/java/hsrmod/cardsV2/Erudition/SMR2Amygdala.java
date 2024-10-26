package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.SMR2AmygdalaPower;

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
