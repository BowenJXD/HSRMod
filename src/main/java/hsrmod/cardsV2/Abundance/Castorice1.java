package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.DewDropPower;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.utils.RelicEventHelper;

public class Castorice1 extends BaseCard {
    public static final String ID = Castorice1.class.getSimpleName();

    public Castorice1() {
        super(ID);
        tags.add(CustomEnums.REVIVE);
        tags.add(CardTags.HEALING);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(RelicEventHelper.DISCARD_TEXT, cards -> {
            if (cards.isEmpty()) return;
            AbstractCard card = cards.get(0);
            int timesToTrigger = card.selfRetain || card.retain ? 2 : 1;

            for (int i = timesToTrigger; i > 0; i--) {
                if (magicNumber > 0) {
                    addToTop(new DrawCardAction(1));
                }
                addToTop(new ApplyPowerAction(p, p, new DewDropPower(p, block)));
                addToTop(new AddTemporaryHPAction(p, p, block));
            }
            addToTop(new DiscardCardsAction(cards));
        }));
    }

    @Override
    public void switchedStance() {
        super.switchedStance();
        addToTop(new DiscardToHandAction(this));
    }
}
