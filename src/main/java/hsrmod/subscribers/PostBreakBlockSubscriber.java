package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PostBreakBlockSubscriber extends IHSRSubscriber {
    void receivePostBreakBlock(AbstractCreature c);
}
