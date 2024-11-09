package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import hsrmod.actions.ElementalDamageAction;

public interface PreElementalDamageSubscriber extends IHSRSubscriber {
    float preElementalDamage(ElementalDamageAction action, float dmg);
}
