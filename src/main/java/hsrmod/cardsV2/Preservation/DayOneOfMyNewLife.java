package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

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
        int plate = magicNumber - ModHelper.getPowerCount(p, PlatedArmorPower.POWER_ID);
        if (plate > 0) addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, plate), plate));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, 4));
    }
}
