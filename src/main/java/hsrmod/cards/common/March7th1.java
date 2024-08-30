package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.only.ReinforcePower;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;

public class March7th1 extends BaseCard {
    public static final String ID = March7th1.class.getSimpleName();
    
    public March7th1() {
        super(ID);
        this.cardsToPreview = new March7th2();
    }

    @Override
    public void upgrade() {
        cardsToPreview.upgrade();
        super.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new ReinforcePower(p, 1, upgraded ? block : 0), 1));
        
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                addToBot(new ApplyPowerAction(p, p, power, -1));
                ModHelper.addToBotAbstract(() -> {
                    if (power.amount <= 0) {
                        addToBot(new RemoveSpecificPowerAction(p, p, power));
                    }
                });
                break;
            }
        }
    }
}
