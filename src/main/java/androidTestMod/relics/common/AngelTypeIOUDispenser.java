package androidTestMod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RelicEventHelper;

import java.util.function.Predicate;

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
                && AbstractDungeon.getCurrRoom() != currRoom) {
            boolean b = false;
            for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
                if (reward.type == RewardItem.RewardType.GOLD) {
                    b = true;
                    break;
                }
            }
            if (b) {
                flash();
                AbstractDungeon.combatRewardScreen.rewards.removeIf(new Predicate<RewardItem>() {
                    @Override
                    public boolean test(RewardItem rewardItem) {
                        return rewardItem.type == RewardItem.RewardType.GOLD;
                    }
                });
                currRoom = AbstractDungeon.getCurrRoom();
                if (reduceCounterAndCheckDestroy()) {
                    doubleGold();
                }
            }
        }
    }
    
    void doubleGold() {
        RelicEventHelper.gainGold(AbstractDungeon.player.gold);
    }
}
