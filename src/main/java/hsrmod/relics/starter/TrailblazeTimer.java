package hsrmod.relics.starter;

import hsrmod.relics.BaseRelic;

public class TrailblazeTimer extends BaseRelic {
    public static final String ID = TrailblazeTimer.class.getSimpleName();

    public TrailblazeTimer() {
        super(ID);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }
}
