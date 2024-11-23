package hsrmod.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface PostUpgradeSubscriber extends IHSRSubscriber{
    void postUpgrade(AbstractCard card);
}
