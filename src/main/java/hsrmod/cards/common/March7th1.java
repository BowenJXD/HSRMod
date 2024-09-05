package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.CleanAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ReinforcePower;

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
        addToBot(new CleanAction(p, 1, false));
    }
}
