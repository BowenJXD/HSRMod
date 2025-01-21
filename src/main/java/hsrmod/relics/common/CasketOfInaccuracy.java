package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Iterator;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    public void onEquip() {
        flash();
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        } else if (!AbstractDungeon.getCurrRoom().rewardTime) {
            AbstractDungeon.getCurrRoom().addCardToRewards();
        } else {
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
        }
    }
}
