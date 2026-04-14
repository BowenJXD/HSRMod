package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.GAMManager;
import hsrmod.utils.ModHelper;

public class OdeToSkyModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToSkyModifier.class.getSimpleName());
    String description;
    
    public OdeToSkyModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToSkyModifier(description);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        GAMManager.addParallelAction(ID, action -> {
            if (action instanceof EvokeOrbAction && AbstractDungeon.player.orbs.get(0) instanceof Frost) {
                addToTop(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, c -> c == card));
            }
            return false;
        });
    }

    @Override
    public void onRemove(AbstractCard card) {
        super.onRemove(card);
        GAMManager.removeParallelAction(ID);
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
