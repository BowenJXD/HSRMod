package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PostHPUpdateSubscriber extends IHSRSubscriber {
    void postHPUpdate(AbstractCreature creature);
}
