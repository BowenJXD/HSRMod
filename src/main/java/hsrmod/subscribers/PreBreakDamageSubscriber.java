package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PreBreakDamageSubscriber extends IHSRSubscriber {
    float preBreakDamage(float amount, AbstractCreature target);
}
