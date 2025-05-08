package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;
import java.util.function.Consumer;

public class DimensionReductionDice extends BaseRelic {
    public static final String ID = DimensionReductionDice.class.getSimpleName();
    
    public DimensionReductionDice() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (!usedUp)
            RewardEditor.addExtraRewardToTop(new Consumer<List<RewardItem>>() {
                @Override
                public void accept(List<RewardItem> rewards) {
                    DimensionReductionDice.this.processRewards(rewards);
                }
            });
    }

    void processRewards(List<RewardItem> rewards) {
        rewards.add(new RewardItem());
        reduceCounterAndCheckDestroy();
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        if (usedUp) return numberOfCards;
        return --numberOfCards;
    }
}
