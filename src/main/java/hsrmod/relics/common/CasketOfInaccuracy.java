/*
package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        // AbstractDungeon.getCurrRoom().addCardReward(new RewardItem(HSR_PINK));
        // AbstractDungeon.getCurrRoom().addCardToRewards();
        // AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(HSR_PINK));
        // AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        // AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
        AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    }
}
*/
