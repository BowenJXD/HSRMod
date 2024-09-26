package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBlockGainSubscriber extends IHSRSubscriber{
    int preBlockGain(AbstractCreature creature, int blockAmount);
}
