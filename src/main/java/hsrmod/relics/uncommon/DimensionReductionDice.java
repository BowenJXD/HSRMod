package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.List;

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
            RewardEditor.addExtraRewardToTop(this::processRewards);
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
