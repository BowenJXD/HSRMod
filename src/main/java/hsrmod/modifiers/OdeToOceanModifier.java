package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.utils.ModHelper;

public class OdeToOceanModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToOceanModifier.class.getSimpleName());
    String description;
    
    public OdeToOceanModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToOceanModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        ModHelper.addToBotAbstract(() -> {
            for (AbstractPower power : target.powers) {
                if (power instanceof BleedingPower) {
                    addToTop(new TriggerPowerAction(power));
                }
            }
        });
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
