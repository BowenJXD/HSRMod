package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;

public class PhantomWorker extends BaseCard {
    public static final String ID = PhantomWorker.class.getSimpleName();

    public PhantomWorker() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, energyOnUse));

        if (upgraded)
            addToBot(new SelectCardsInHandAction(energyOnUse, cardStrings.EXTENDED_DESCRIPTION[0], true, true,
                    AbstractCard::canUpgrade, cards -> {
                for (AbstractCard card : cards)
                    addToBot(new UpgradeSpecificCardAction(card));
            }));
        else
            for (int i = 0; i < energyOnUse; i++)
                addToBot(new UpgradeRandomCardAction());
    }
}
