package hsrmod.subscribers;

import hsrmod.actions.ElementalDamageAction;

/**
 * Don't use to modify toughness, but can be used to clear tr. 
 */
public interface PreElementalDamageSubscriber extends IHSRSubscriber {
    float preElementalDamage(ElementalDamageAction action, float dmg);
}
