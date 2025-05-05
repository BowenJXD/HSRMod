package androidTestMod.relics.uncommon;

import androidTestMod.relics.BaseRelic;
import androidTestMod.relics.starter.TrailblazeTimer;
import androidTestMod.subscribers.INumChangerSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;

public class IndecipherableBox extends BaseRelic implements INumChangerSubscriber {
    public static final String ID = IndecipherableBox.class.getSimpleName();

    public IndecipherableBox() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        if (ModHelper.hasRelic(TrailblazeTimer.ID)) {
            return ++numberOfCards;
        }
        return numberOfCards;
    }

    @Override
    public float changeNum(float base) {
        if (SubscriptionManager.checkSubscriber(this)) {
            return base * 1.5f;
        }
        return base;
    }

    @Override
    public SubscriptionManager.NumChangerType getSubType() {
        return SubscriptionManager.NumChangerType.WAX_WEIGHT;
    }
}
