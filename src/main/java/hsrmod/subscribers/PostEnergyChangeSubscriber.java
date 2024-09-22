package hsrmod.subscribers;

import basemod.interfaces.ISubscriber;

public interface PostEnergyChangeSubscriber extends ISubscriber {
    int receivePostEnergyChange(int changeAmount);
}
