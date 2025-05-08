package hsrmod.cards.common;

import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Qingque1 extends BaseCard {
    public static final String ID = Qingque1.class.getSimpleName();

    private int drawAmount = 1;
    private int costCache = 0;
    private int energyAmount = 1;

    public Qingque1() {
        super(ID);
        returnToHand = true;
        costCache = cost;
    }

    @Override
    public void upgrade() {
        super.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = AbstractDungeon.cardRandomRng.random(100) <= magicNumber;
        if (returnToHand) {
            exhaust = false;
        } else {
            exhaust = AbstractDungeon.cardRandomRng.random(100) <= magicNumber;
        }

        if (AbstractDungeon.cardRandomRng.random(100) <= magicNumber) {
            addToBot(new GainEnergyAction(costForTurn));
        }

        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() > 0) {
            addToBot(new DrawCardAction(p, drawAmount));
        }

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                Qingque1.this.updateCost(costCache - cost);
            }
        });
    }
}
