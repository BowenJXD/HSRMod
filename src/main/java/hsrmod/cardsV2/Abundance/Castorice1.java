package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.DiscardCardsAction;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.utils.RelicEventHelper;

public class Castorice1 extends BaseCard {
    public static final String ID = Castorice1.class.getSimpleName();

    public Castorice1() {
        super(ID);
        tags.add(CustomEnums.REVIVE);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(p.hand.size(), RelicEventHelper.DISCARD_TEXT, true, true, c -> true, cards -> {
            int heal = 0;
            int draw = 0;
            for (AbstractCard card : cards) {
                heal += magicNumber;
                if (card.selfRetain || card.retain) {
                    heal += magicNumber;
                    draw += 2;
                }
            }
            addToTop(new ApplyPowerAction(p, p, new NecrosisPower(p, 1)));
            addToTop(new TriggerPowerAction(p.getPower(NecrosisPower.POWER_ID)));
            if (heal > 0) {
                addToTop(new AddTemporaryHPAction(p, p, heal));
            }
            if (draw > 0) {
                addToTop(new DrawCardAction(p, draw));
            }
            addToTop(new DiscardCardsAction(cards));
        }));
    }

}
