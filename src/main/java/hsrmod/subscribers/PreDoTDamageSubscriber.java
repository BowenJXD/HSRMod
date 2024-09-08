package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.powers.misc.DoTPower;

public interface PreDoTDamageSubscriber extends ISubscriber {
    float preDoTDamage(float amount, AbstractCreature target, DoTPower power); 
}
