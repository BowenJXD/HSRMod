package androidTestMod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.RandomCardFromDrawPileToHandAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.utils.ModHelper;

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
            ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    FungalPustule.this.modifyCostForCombat(1);
                    FungalPustule.this.upgradeMagicNumber(1);
                }
            });
        }
    }
}
