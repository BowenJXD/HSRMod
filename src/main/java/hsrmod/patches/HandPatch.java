package hsrmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;

import java.util.ArrayList;
import java.util.List;

public class HandPatch {
    @SpirePatch(clz = CardGroup.class, method = "refreshHandLayout")
    public static class RefreshHandLayoutPatch {
        public static List<AbstractCard> handCache = new ArrayList<>();
        
        @SpirePostfixPatch
        public static void postfix(CardGroup __inst) {
            List<AbstractCard> hand = new ArrayList<>(AbstractDungeon.player.hand.group);

            // Find new cards added to hand
            for (AbstractCard card : hand) {
                if (!handCache.contains(card) && card instanceof BaseCard) {
                    ((BaseCard) card).onEnterHand();
                    ((BaseCard) card).inHand = true;
                }
            }

            // Find cards removed from hand
            for (AbstractCard card : handCache) {
                if (!hand.contains(card) && card instanceof BaseCard 
                        && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                    ((BaseCard) card).onLeaveHand();
                    ((BaseCard) card).inHand = false;
                }
            }

            // Update handCache to the current hand
            handCache = hand;
        }
    }
    
    @SpirePatch(clz = HandCheckAction.class, method = "update")
    public static class HandCheckPatch {
        @SpirePostfixPatch
        public static void postfix(HandCheckAction action) {
            if (action.isDone) {
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }
    }
}
