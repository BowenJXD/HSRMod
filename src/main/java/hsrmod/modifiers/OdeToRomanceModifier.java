package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class OdeToRomanceModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToRomanceModifier.class.getSimpleName());
    String description;
    
    public OdeToRomanceModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToRomanceModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        int count = (int) AbstractDungeon.actionManager.orbsChanneledThisCombat.stream().filter(o -> Objects.equals(o.ID, Lightning.ORB_ID)).count();
        if (AbstractDungeon.player.filledOrbCount() <= 0) return;
        for (int i = 0; i < count; i++) {
            int r = AbstractDungeon.cardRandomRng.random(AbstractDungeon.player.filledOrbCount() - 1);
            AbstractOrb orb = AbstractDungeon.player.orbs.get(r);
            addToBot(new TriggerPassiveAction(orb));
        }
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
