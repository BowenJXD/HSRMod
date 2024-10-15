package hsrmod.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * Called when use() is called on a card. Defaults to be false, will make card usable if true (ignoring energy check). |=.
 */
public interface ICheckUsableSubscriber extends IHSRSubscriber{
    boolean checkUsable(AbstractCard card);
}
