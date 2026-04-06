package hsrmod.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GAMManager;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;

import java.util.ArrayList;
import java.util.List;

public class DancePartnersModifier extends AbstractCardModifier {
    public static final String ID = HSRMod.makePath(DancePartnersModifier.class.getSimpleName());

    int trMod;
    int superBreakPercent;
    String description;

    public DancePartnersModifier(int trMod, int superBreakPercent, String description) {
        this.trMod = trMod;
        this.superBreakPercent = superBreakPercent;
        this.description = description;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + description;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        GAMManager.addParallelAction(ID, action -> {
            if (!(action instanceof ElementalDamageAction))
                return false;

            ElementalDamageAction eda = (ElementalDamageAction) action;
            if (eda.info == null)
                return false;

            BaseCard baseCard = eda.info.card;
            if (baseCard != null && baseCard == card && baseCard.tr > 0) {
                addToBot(new BreakDamageAction(eda.target, new DamageInfo(AbstractDungeon.player, baseCard.tr), superBreakPercent / 100f));
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
        return !CardModifierManager.hasModifier(card, ID) && card instanceof BaseCard;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        super.onCalculateCardDamage(card, mo);
        if (card instanceof BaseCard) {
            BaseCard baseCard = (BaseCard) card;
            baseCard.tr += trMod;
            baseCard.isTrModified = true;
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DancePartnersModifier(trMod, superBreakPercent, description);
    }
}
