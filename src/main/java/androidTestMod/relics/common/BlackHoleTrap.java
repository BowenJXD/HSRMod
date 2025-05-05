package androidTestMod.relics.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RewardEditor;

import java.util.List;

public class BlackHoleTrap extends BaseRelic {
    public static final String ID = BlackHoleTrap.class.getSimpleName();
    
    public static int staticMagicNumber = 100;
    
    public BlackHoleTrap() {
        super(ID);
        staticMagicNumber = magicNumber;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        RewardEditor.addExtraRewardToBot(this::processRewards);
    }

    void processRewards(List<RewardItem> rewards) {
        for (RewardItem reward : rewards) {
            if (reward.type == RewardItem.RewardType.GOLD) {
                reward.incrementGold(getAmount());
                flash();
                break;
            }
        }
    }
    
    static int getAmount() {
        AbstractPlayer p = AbstractDungeon.player;
        return Math.max(0, (int) (p.currentHealth * 100f / p.maxHealth - staticMagicNumber));
    }
}
