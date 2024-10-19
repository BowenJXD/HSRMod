package hsrmod.subscribers;

import hsrmod.actions.ElementalDamageAction;

public interface ISetCritRateSubscriber extends IHSRSubscriber{
    void setCritRate(ElementalDamageAction action);
}
