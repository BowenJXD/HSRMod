package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class FungalPustule extends BaseCard {
    public static final String ID = FungalPustule.class.getSimpleName();
    
    int energyNum = 2;
    
    public FungalPustule() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(energyNum));
        addToBot(new DrawCardAction(magicNumber));
        ModHelper.addToBotAbstract(() -> {
            if (upgraded) {
                modifyCostForCombat(1);
                energyNum++;
            }
        });
    }
}
