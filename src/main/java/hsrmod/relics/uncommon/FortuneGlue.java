package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.List;

public class FortuneGlue extends BaseRelic {
    public static final String ID = FortuneGlue.class.getSimpleName();

    public FortuneGlue() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (!usedUp)
            RewardEditor.addExtraRewardToBot(this::processRewards);
    }
    
    public void processRewards(List<RewardItem> rewards) {
        boolean doFlash = false;
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.CARD && !reward.cards.isEmpty()) {
                RewardEditor.setRewardCards(reward, AbstractCard.CardRarity.RARE);
                doFlash = true;
                reduceCounterAndCheckDestroy();
                break;
            }
        }
        if (doFlash) flash();
    }
}
