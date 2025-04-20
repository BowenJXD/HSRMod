package hsrmod.subscribers;

import hsrmod.powers.BasePower;

public interface PrePowerTriggerSubscriber extends IHSRSubscriber{
    void prePowerTrigger(BasePower power);
}
