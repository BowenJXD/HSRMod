package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CleftSingularity extends BaseCard {
    public static final String ID = CleftSingularity.class.getSimpleName();
    
    public CleftSingularity() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
    }
}
