package hsrmod.patches;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.subscribers.SubscriptionManager;

import java.util.ArrayList;
import java.util.List;

public class CardGroupPatch {
    @SpirePatch(clz = CardGroup.class, method = "addToHand")
    @SpirePatch(clz = CardGroup.class, method = "addToTop")
    @SpirePatch(clz = CardGroup.class, method = "addToBottom")
    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot")
    public static class AddPatch {
        public static void Postfix(CardGroup __inst, AbstractCard c) {
            if (AbstractDungeon.actionManager.currentAction instanceof RestoreRetainedCardsAction)
                return;
            SubscriptionManager.getInstance().triggerPostCardMove(__inst, c, true);
            triggerMove(__inst, c, true);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
    @SpirePatch(clz = CardGroup.class, method = "resetCardBeforeMoving")
    public static class RemovePatch {
        public static void Postfix(CardGroup __inst, AbstractCard c) {
            SubscriptionManager.getInstance().triggerPostCardMove(__inst, c, false);
            triggerMove(__inst, c, false);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeTopCard")
    public static class RemoveTopPatch {
        public static AbstractCard cardCache;

        public static void Prefix(CardGroup __inst) {
            cardCache = __inst.getTopCard();
        }

        public static void Postfix(CardGroup __inst) {
            if (cardCache != null) {
                SubscriptionManager.getInstance().triggerPostCardMove(__inst, cardCache, false);
                triggerMove(__inst, cardCache, false);
                cardCache = null;
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypes = "java.lang.String")
    public static class RemoveByIdPatch {
        public static AbstractCard cardCache;

        public static void Prefix(CardGroup __inst, String targetID) {
            cardCache = __inst.findCardById(targetID);
        }

        public static void Postfix(CardGroup __inst, String targetID) {
            if (cardCache != null) {
                SubscriptionManager.getInstance().triggerPostCardMove(__inst, cardCache, false);
                triggerMove(__inst, cardCache, false);
                cardCache = null;
            }
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class EmptyDeckShufflePatch {
        static List<AbstractCard> groupCache;

        @SpirePrefixPatch
        public static void Prefix(EmptyDeckShuffleAction __inst) {
            if (groupCache == null) {
                groupCache = new ArrayList<>(AbstractDungeon.player.discardPile.group);
            }
        }

        @SpirePostfixPatch
        public static void Postfix(EmptyDeckShuffleAction __inst) {
            if (__inst.isDone && groupCache != null) {
                CardGroup group = AbstractDungeon.player.discardPile;
                for (AbstractCard card : groupCache) {
                    SubscriptionManager.getInstance().triggerPostCardMove(group, card, false);
                    triggerMove(group, card, false);
                }
                groupCache = null;
            }
        }
    }

    public static void triggerMove(CardGroup __inst, AbstractCard card, boolean in) {
        if (card instanceof BaseCard) {
            BaseCard huTaoCard = (BaseCard) card;
            huTaoCard.onMove(__inst, in);
        }
    }
}
