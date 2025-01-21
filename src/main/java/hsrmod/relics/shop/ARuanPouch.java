package hsrmod.relics.shop;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

import java.util.List;

public class ARuanPouch extends BaseRelic {
    public static final String ID = ARuanPouch.class.getSimpleName();

    public ARuanPouch() {
        super(ID);
        setCounter(magicNumber);
    }

    public void onEquip() {
        flash();
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            for(int i = 0; i < 2; ++i) {
                AbstractDungeon.getCurrRoom().addCardToRewards();
            }
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        } else if (!AbstractDungeon.getCurrRoom().rewardTime) {
            for (int i = 0; i < 3; ++i) {
                AbstractDungeon.getCurrRoom().addCardToRewards();
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
            }
        }
        ModHelper.addEffectAbstract(this::reduceCounterAndCheckUsedUp); 
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        if (usedUp) return numberOfCards;
        if (counter == magicNumber) return numberOfCards;
        reduceCounterAndCheckUsedUp();
        return 1;
    }
}
