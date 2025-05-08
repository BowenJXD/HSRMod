package hsrmod.subscribers;

import hsrmod.modcore.ElementType;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreToughnessReduceSubscriber extends IHSRSubscriber {
    float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType);
}
