package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RelicEventHelper;

public class AngelTypeIOUDispenser extends BaseRelic {
    public static final String ID = AngelTypeIOUDispenser.class.getSimpleName();

    AbstractRoom currRoom;
    
    public AngelTypeIOUDispenser() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void update() {
        super.update();
        if (counter > 0 
                && isObtained
                && AbstractDungeon.currMapNode != null
                && AbstractDungeon.getCurrRoom().isBattleOver
                && AbstractDungeon.getCurrRoom() != currRoom
                && AbstractDungeon.combatRewardScreen.rewards.stream().anyMatch(rewardItem -> rewardItem.type == RewardItem.RewardType.GOLD)) {
            flash();
            AbstractDungeon.combatRewardScreen.rewards.removeIf(rewardItem -> rewardItem.type == RewardItem.RewardType.GOLD);
            currRoom = AbstractDungeon.getCurrRoom();
            if (reduceCounterAndCheckDestroy()) {
                doubleGold();
            }
        }
    }
    
    void doubleGold() {
        RelicEventHelper.gainGold(AbstractDungeon.player.gold);
    }
}
