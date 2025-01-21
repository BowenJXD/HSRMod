package hsrmod.relics.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.List;

public class BlackHoleTrap extends BaseRelic {
    public static final String ID = BlackHoleTrap.class.getSimpleName();
    
    public BlackHoleTrap() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        RewardEditor.addExtraRewardToBot(this::processRewards);
    }

    void processRewards(List<RewardItem> rewards) {
        boolean doFlash = false;
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.GOLD) {
                reward.incrementGold(getAmount());
                doFlash = true;
                break;
            }
        }
        if (doFlash) flash();
    }
    
    int getAmount() {
        AbstractPlayer p = AbstractDungeon.player;
        return Math.max(0, (int) (p.currentHealth * 100f / p.maxHealth - magicNumber));
    }
}
