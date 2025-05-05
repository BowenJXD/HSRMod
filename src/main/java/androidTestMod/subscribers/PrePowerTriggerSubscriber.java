package androidTestMod.subscribers;

import androidTestMod.powers.BasePower;

public interface PrePowerTriggerSubscriber extends IHSRSubscriber{
    void prePowerTrigger(BasePower power);
}
