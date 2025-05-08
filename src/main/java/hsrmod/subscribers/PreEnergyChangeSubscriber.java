package hsrmod.subscribers;

public interface PreEnergyChangeSubscriber extends IHSRSubscriber {
    int preEnergyChange(int changeAmount);
}
