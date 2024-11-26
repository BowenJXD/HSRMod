package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class Qingque1 extends BaseCard {
    public static final String ID = Qingque1.class.getSimpleName();
    
    private int drawAmount = 1;
    private int costCache = 0;
    private int energyAmount = 1;
    
    public Qingque1() {
        super(ID);
        returnToHand = true;
        exhaust = true;
        costCache = cost;
    }

    @Override
    public void upgrade() {
        super.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = AbstractDungeon.cardRandomRng.random(100) <= magicNumber;
        exhaust = !returnToHand;
        
        if (AbstractDungeon.cardRandomRng.random(100) <= magicNumber) {
            addToBot(new GainEnergyAction(costForTurn));
        }
        
        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() > 0) {
            addToBot(new DrawCardAction(p, drawAmount));
        }

        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
    }
}
