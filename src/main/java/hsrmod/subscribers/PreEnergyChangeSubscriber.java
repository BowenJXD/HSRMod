package hsrmod.subscribers;

public interface PreEnergyChangeSubscriber extends IHSRSubscriber {
    int receivePreEnergyChange(int changeAmount);
}
