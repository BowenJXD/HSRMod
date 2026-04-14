package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class OdeToEarthModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToEarthModifier.class.getSimpleName());
    String description;
    
    public OdeToEarthModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToEarthModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        ModHelper.addToBotAbstract(() -> {
            for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                if (Objects.equals(orb.ID, Frost.ORB_ID)) {
                    addToTop(new TriggerPassiveAction(orb));
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
