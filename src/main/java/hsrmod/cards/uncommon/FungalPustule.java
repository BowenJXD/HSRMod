package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.RandomCardFromDrawPileToHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;

public class FungalPustule extends BaseCard {
    public static final String ID = FungalPustule.class.getSimpleName();

    public FungalPustule() {
        super(ID);
        energyCost = 60;
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToTop(new RandomCardFromDrawPileToHandAction());
        addToBot(new GainEnergyAction(magicNumber));
        if (upgraded) {
            ModHelper.addToBotAbstract(() -> {
                modifyCostForCombat(1);
                upgradeMagicNumber(1);
            });
        }
    }
}
