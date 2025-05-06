package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;
import java.util.function.Consumer;

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
        RewardEditor.addExtraRewardToBot(new Consumer<List<RewardItem>>() {
            @Override
            public void accept(List<RewardItem> rewards) {
                SocietyTicket.this.processRewards(rewards);
            }
        });
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
