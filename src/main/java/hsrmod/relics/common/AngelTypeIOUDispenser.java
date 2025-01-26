package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;

public class AngelTypeIOUDispenser extends BaseRelic {
    public static final String ID = AngelTypeIOUDispenser.class.getSimpleName();

    AbstractRoom currRoom;
    
    public AngelTypeIOUDispenser() {
        super(ID);
    }
    
    public AngelTypeIOUDispenser(int initialCounter) {
        super(ID);
        magicNumber = initialCounter;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        counter = magicNumber;
    }

    @Override
    public void update() {
        super.update();
        if (counter > 0 
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
        AbstractDungeon.player.gainGold(AbstractDungeon.player.gold);
    }
}
