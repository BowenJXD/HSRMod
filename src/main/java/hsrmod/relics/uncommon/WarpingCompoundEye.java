package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;
import java.util.function.Consumer;

public class WarpingCompoundEye extends BaseRelic {
    public static final String ID = WarpingCompoundEye.class.getSimpleName();

    public WarpingCompoundEye() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        RewardEditor.addExtraRewardToBot(new Consumer<List<RewardItem>>() {
            @Override
            public void accept(List<RewardItem> rewards) {
                WarpingCompoundEye.this.processRewards(rewards);
            }
        });
    }

    public void processRewards(List<RewardItem> rewards) {
        boolean doFlash = false;
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.CARD && !reward.cards.isEmpty()) {
                for (int i = 0; i < reward.cards.size(); i++) {
                    if (!reward.cards.get(i).upgraded
                            && reward.cards.get(i).canUpgrade()
                            && reward.cards.get(i).rarity == AbstractCard.CardRarity.COMMON) {
                        reward.cards.get(i).upgrade();
                        doFlash = true;
                    }
                }
            }
        }
        if (doFlash) flash();
    }
}
