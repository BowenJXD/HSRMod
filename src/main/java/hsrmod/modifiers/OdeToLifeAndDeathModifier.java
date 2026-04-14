package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.uniqueBuffs.NewbudPower;

public class OdeToLifeAndDeathModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToLifeAndDeathModifier.class.getSimpleName());
    String description;
    
    public OdeToLifeAndDeathModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToLifeAndDeathModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NewbudPower(AbstractDungeon.player, card.magicNumber)));
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
