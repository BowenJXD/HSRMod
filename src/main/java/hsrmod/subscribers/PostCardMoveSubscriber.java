package hsrmod.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public interface PostCardMoveSubscriber extends IHSRSubscriber {
    void postCardMove(CardGroup group, AbstractCard card, boolean in);
}
