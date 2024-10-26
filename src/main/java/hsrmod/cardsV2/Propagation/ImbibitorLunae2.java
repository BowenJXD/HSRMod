package hsrmod.cardsV2.Propagation;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;

public class ImbibitorLunae2 extends BaseCard {
    public static final String ID = ImbibitorLunae2.class.getSimpleName();
    
    public ImbibitorLunae2() {
        super(ID);
        energyCost = 140;
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0], true, true, c -> true, cards -> {
            if (!cards.isEmpty()) {
                for (AbstractCard c : cards) {
                    addToTop(new ExhaustSpecificCardAction(c, p.hand));
                }
                addToTop(new GainEnergyAction(2));
                addToBot(new MoveCardsAction(p.hand, p.discardPile, c -> c == this));
            }
        }));
    }
}
