package hsrmod.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreFollowUpSubscriber extends IHSRSubscriber {
    AbstractCreature preFollowUpAction(AbstractCard card, AbstractCreature target);
}
