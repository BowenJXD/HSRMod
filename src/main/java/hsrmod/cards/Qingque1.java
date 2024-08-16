package hsrmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.blue.HelloWorld;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.KeepCardAction;
import hsrmod.actions.ReduceECByHandCardNumAction;

import java.util.Random;

public class Qingque1 extends BaseCard{
    public static final String ID = Qingque1.class.getSimpleName();
    
    private int drawAmount = 1;
    
    private int energyAmount = 1;
    
    public Qingque1() {
        super(ID);
        returnToHand = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, drawAmount));
        if (AbstractDungeon.cardRandomRng.random(100) <= magicNumber) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energyAmount));
        }
        
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            this.addToTop(new EmptyDeckShuffleAction());
        }
    }
}
