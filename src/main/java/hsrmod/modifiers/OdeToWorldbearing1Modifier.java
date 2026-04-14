package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.modcore.HSRMod;

public class OdeToWorldbearing1Modifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToWorldbearing1Modifier.class.getSimpleName());
    String description;
    
    public OdeToWorldbearing1Modifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToWorldbearing1Modifier(description);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
