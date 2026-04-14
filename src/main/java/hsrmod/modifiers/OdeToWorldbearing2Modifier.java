package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hsrmod.modcore.HSRMod;

public class OdeToWorldbearing2Modifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToWorldbearing2Modifier.class.getSimpleName());
    String description;
    
    public OdeToWorldbearing2Modifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToWorldbearing2Modifier(description);
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
