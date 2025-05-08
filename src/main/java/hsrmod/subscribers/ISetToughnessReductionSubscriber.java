package hsrmod.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface ISetToughnessReductionSubscriber extends IHSRSubscriber {
    int setToughnessReduction(AbstractCreature target, int amt);
}
