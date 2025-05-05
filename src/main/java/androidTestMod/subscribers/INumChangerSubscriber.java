package androidTestMod.subscribers;

public interface INumChangerSubscriber extends IHSRSubscriber {
    float changeNum(float base);

    SubscriptionManager.NumChangerType getSubType();
}
