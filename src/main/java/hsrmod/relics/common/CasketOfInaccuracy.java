package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RewardEditor;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    public void onEquip() {
        flash();
        if (AbstractDungeon.getCurrRoom().rewardTime && AbstractDungeon.isScreenUp) {
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
                }
            });
        } else if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
            if (!(AbstractDungeon.getCurrRoom() instanceof ShopRoom)) {
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
            }
        } else {
            RewardEditor.addExtraCardRewardToTop();
        }
    }
}
