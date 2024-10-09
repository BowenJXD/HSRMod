package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.CleanAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.HuohuoPower;
import hsrmod.powers.uniqueBuffs.ReinforcePower;

public class March7th1 extends BaseCard {
    public static final String ID = March7th1.class.getSimpleName();
    
    public March7th1() {
        super(ID);
        this.cardsToPreview = new March7th2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        AbstractPower power = p.getPower(ReinforcePower.POWER_ID);
        if (power != null)
            ((ReinforcePower) power).block = upgraded ? block : 0;
        else
            addToBot(new ApplyPowerAction(p, p, new ReinforcePower(p, upgraded ? block : 0)));
        addToBot(new CleanAction(p, 1, false));
    }
}
