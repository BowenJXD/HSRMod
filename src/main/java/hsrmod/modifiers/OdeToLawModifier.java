package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.uniqueBuffs.MilitaryMeritPower;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class OdeToLawModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(OdeToLawModifier.class.getSimpleName());
    String description;
    
    public OdeToLawModifier(String description) {
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OdeToLawModifier(description);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        ModHelper.addToBotAbstract(() -> {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (Objects.equals(power.ID, MilitaryMeritPower.POWER_ID)) {
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
