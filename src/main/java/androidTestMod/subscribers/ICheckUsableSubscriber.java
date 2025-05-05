package androidTestMod.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * Called when BaseCard::canUse is called on a card. Defaults to be true, will make card usable if true. &=.
 */
public interface ICheckUsableSubscriber extends IHSRSubscriber{
    boolean checkUsable(AbstractCard card);
}
