package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.List;

public class SocietyTicket extends BaseRelic {
    public static final String ID = SocietyTicket.class.getSimpleName();
    
    public SocietyTicket() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        RewardEditor.addExtraRewardToBot(this::processRewards);
    }

    public void processRewards(List<RewardItem> rewards) {
        boolean doFlash = false;
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.GOLD) {
                reward.incrementGold((int) (reward.goldAmt * magicNumber / 100f));
                doFlash = true;
                break;
            }
        }
        if (doFlash) flash();
    }
}
