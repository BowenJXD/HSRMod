package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

import javax.swing.undo.CannotRedoException;

public class AntinomyModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(AntinomyModifier.class.getSimpleName());

    int dmg;
    String description;
    
    public AntinomyModifier(int dmg, String description) {
        this.dmg = dmg;
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + description;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        card.freeToPlayOnce = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        super.onRemove(card);
        card.freeToPlayOnce = false;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, dmg)));
        ModHelper.addToBotAbstract(() -> CardModifierManager.removeSpecificModifier(card, this, false));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return null;
    }
}
