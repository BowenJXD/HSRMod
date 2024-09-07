package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBreakDamageSubscriber extends ISubscriber {
    float preBreakDamage(float amount, AbstractCreature target);
}
