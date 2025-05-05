package androidTestMod.relics.common;

import com.megacrit.cardcrawl.rewards.RewardItem;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;

import java.util.List;

public class SocietyTicket extends BaseRelic {
    public static final String ID = SocietyTicket.class.getSimpleName();
    
    public static int staticMagicNumber = 100;
    
    public SocietyTicket() {
        super(ID);
        staticMagicNumber = magicNumber;
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
                reward.incrementGold((int) (reward.goldAmt * staticMagicNumber / 100f));
                doFlash = true;
                break;
            }
        }
        if (doFlash) flash();
    }
}
