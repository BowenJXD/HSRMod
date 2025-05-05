package androidTestMod.relics.uncommon;

import com.megacrit.cardcrawl.rewards.RewardItem;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;

import java.util.List;

public class BeaconColoringPaste extends BaseRelic {
    public static final String ID = BeaconColoringPaste.class.getSimpleName();

    public BeaconColoringPaste() {
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
            if (reward.type == RewardItem.RewardType.CARD && !reward.cards.isEmpty()) {
                for (int i = 0; i < reward.cards.size(); i++) {
                    if (!reward.cards.get(i).upgraded
                            && reward.cards.get(i).canUpgrade()) {
                        reward.cards.get(i).upgrade();
                        doFlash = true;
                        break;
                    }
                }
            }
        }
        if (doFlash) flash();
    }
}
