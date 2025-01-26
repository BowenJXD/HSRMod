/*
package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.INumChangerSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

public class ManMadeMeteorite extends BaseRelic implements INumChangerSubscriber {
    public static final String ID = ManMadeMeteorite.class.getSimpleName();
    
    public ManMadeMeteorite() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    public void onEquip() {
        flash();
        if (AbstractDungeon.getCurrRoom().rewardTime) {
            ModHelper.addEffectAbstract(() -> {
                RewardItem reward = new RewardItem();
                RewardEditor.getInstance().setRewardCards(reward);
                AbstractDungeon.combatRewardScreen.rewards.add(reward);
            });
        } else if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
            ModHelper.addEffectAbstract(() -> {
                if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                    RewardItem reward = new RewardItem();
                    RewardEditor.getInstance().setRewardCards(reward);
                    AbstractDungeon.combatRewardScreen.rewards.add(reward);
                }
            });
        } else {
            RewardItem reward = new RewardItem();
            RewardEditor.getInstance().setRewardCards(reward);
            AbstractDungeon.getCurrRoom().rewards.add(0, reward);
        }
        counter = -2;
    }

    @Override
    public float changeNum(float base) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (counter == -2) SubscriptionManager.getInstance().unsubscribeLater(this);
            else return 100;
        }
        return base;
    }

    @Override
    public SubscriptionManager.NumChangerType getSubType() {
        return SubscriptionManager.NumChangerType.WAX_WEIGHT;
    }
}
*/
