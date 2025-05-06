package androidTestMod.relics.uncommon;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;
import java.util.function.Consumer;

public class FortuneGlue extends BaseRelic {
    public static final String ID = FortuneGlue.class.getSimpleName();

    public FortuneGlue() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (!usedUp)
            RewardEditor.addExtraRewardToBot(new Consumer<List<RewardItem>>() {
                @Override
                public void accept(List<RewardItem> rewards) {
                    FortuneGlue.this.processRewards(rewards);
                }
            });
    }
    
    public void processRewards(List<RewardItem> rewards) {
        boolean doFlash = false;
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.CARD && !reward.cards.isEmpty()) {
                RewardEditor.setRewardRarity(reward, AbstractCard.CardRarity.RARE);
                doFlash = true;
            }
        }
        if (doFlash) {
            flash();
            destroy();
        }
    }
}
