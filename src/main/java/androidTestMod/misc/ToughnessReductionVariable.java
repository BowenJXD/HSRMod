package androidTestMod.misc;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import androidTestMod.cards.BaseCard;

public class ToughnessReductionVariable extends DynamicVariable {
    @Override
    public String key() {
        return "TR";
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((BaseCard)card).isTrModified;
        // Set to true if the value is modified from the base value.
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        ((BaseCard)card).isTrModified = v;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof BaseCard) {
            return ((BaseCard) card).tr;
        }
        return 0;
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard abstractCard) {
        if (abstractCard instanceof BaseCard) {
            return ((BaseCard) abstractCard).baseTr;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard abstractCard) {
        return ((BaseCard)abstractCard).upgradedTr;
    }

    public static void setBaseValue(AbstractCard card, int baseValue) {
        if (card instanceof BaseCard) {
            ((BaseCard) card).baseTr = baseValue;
            ((BaseCard) card).tr = baseValue;
            
        }
    }
}
