package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

public class OdeToTimeModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToTimeModifier.class.getSimpleName());
    String description;
    
    public OdeToTimeModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToTimeModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        ModHelper.addToBotAbstract(() -> {
            for (int i = 1; i < AbstractDungeon.player.filledOrbCount(); i++) {
                ModHelper.triggerPassiveTo(AbstractDungeon.player.orbs.get(i), target);
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
