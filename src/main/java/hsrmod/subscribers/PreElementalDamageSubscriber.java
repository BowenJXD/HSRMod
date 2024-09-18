package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;
import hsrmod.actions.ElementalDamageAction;

public interface PreElementalDamageSubscriber extends ISubscriber {
    float preElementalDamage(ElementalDamageAction action);
}
