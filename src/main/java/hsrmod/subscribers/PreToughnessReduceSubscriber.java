package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementType;

public interface PreToughnessReduceSubscriber extends IHSRSubscriber {
    float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType);
}
