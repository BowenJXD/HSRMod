package hsrmod.subscribers;

public interface IRunnableSubscriber extends IHSRSubscriber {
    void run();
    
    SubscriptionManager.RunnableType getSubType();
}
