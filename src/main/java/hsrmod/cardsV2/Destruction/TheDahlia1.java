package hsrmod.cardsV2.Destruction;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.modifiers.DancePartnersModifier;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.utils.RelicEventHelper;

public class TheDahlia1 extends BaseCard {
    public static final String ID = TheDahlia1.class.getSimpleName();
    
    public TheDahlia1() {
        super(ID);
        isEthereal = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new BreakEffectPower(p, magicNumber), magicNumber));
        }
        addToBot(new SelectCardsInHandAction(cardStrings.EXTENDED_DESCRIPTION[1],
                card -> {
                    return !CardModifierManager.hasModifier(card, DancePartnersModifier.ID);
                },
                cards -> {
                if (!cards.isEmpty()) {
                    cards.forEach(c -> CardModifierManager.addModifier(c, new DancePartnersModifier(1, 50, cardStrings.EXTENDED_DESCRIPTION[0])));
                }
        }));
    }
}
