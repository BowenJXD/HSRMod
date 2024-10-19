package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import hsrmod.cards.BaseCard;

public class DayOneOfMyNewLife extends BaseCard {
    public static final String ID = DayOneOfMyNewLife.class.getSimpleName();
    
    public DayOneOfMyNewLife() {
        super(ID);
        isInnate = true;
        exhaust = true;
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(PlatedArmorPower.POWER_ID))
            addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber), 0));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, baseBlock));
    }
}
