package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class OdeToTrickeryModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToTrickeryModifier.class.getSimpleName());
    String description;
    
    public OdeToTrickeryModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToTrickeryModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        RelicEventHelper.gainGold(10);
        addToBot(new VFXAction(new GainPennyEffect(target.hb.cX, target.hb.cY)));
        ModHelper.addToBotAbstract(() -> CardModifierManager.removeModifiersById(card, ID, true));
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
