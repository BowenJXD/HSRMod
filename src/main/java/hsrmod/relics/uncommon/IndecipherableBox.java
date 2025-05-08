package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import hsrmod.relics.starter.TrailblazeTimer;
import hsrmod.subscribers.INumChangerSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

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
