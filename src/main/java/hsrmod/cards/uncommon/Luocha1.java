package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.CleanAction;
import hsrmod.cards.BaseCard;

public class Luocha1 extends BaseCard {
    public static final String ID = Luocha1.class.getSimpleName();
    
    public Luocha1() {
        super(ID);
        selfRetain = true;
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        addToBot(new HealAction(p, p, p.maxHealth / 2 < p.currentHealth ? magicNumber : magicNumber * 2));
        addToBot(new CleanAction(p, 1, true));
        addToBot(new SelectCardsInHandAction(upgraded ? p.hand.size() : 1, cardStrings.EXTENDED_DESCRIPTION[0], upgraded, upgraded, card -> true, (cards) -> {
            for (AbstractCard card : cards) {
                addToBot(new ExhaustSpecificCardAction(card, p.hand));
            }
        }));
    }
}
