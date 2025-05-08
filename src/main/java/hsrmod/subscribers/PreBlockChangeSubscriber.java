package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBlockChangeSubscriber extends IHSRSubscriber{
    int preBlockChange(AbstractCreature creature, int blockAmount);
}
