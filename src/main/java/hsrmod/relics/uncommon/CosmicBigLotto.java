package hsrmod.relics.uncommon;

import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;
import java.util.function.Consumer;

public class CosmicBigLotto extends BaseRelic {
    public static final String ID = CosmicBigLotto.class.getSimpleName();

    int winChance = 20;
    int loseChance = 10;

    public CosmicBigLotto() {
        super(ID);
        setCounter(1);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (usedUp) return;
        if (AbstractDungeon.miscRng.random(100) < winChance) {
            flash();
            RewardEditor.addExtraRewardToTop(new Consumer<List<RewardItem>>() {
                @Override
                public void accept(List<RewardItem> rewards) {
                    rewards.add(new RewardItem(RelicLibrary.getRelic(AbstractDungeon.returnRandomRelicKey(AbstractDungeon.returnRandomRelicTier())).makeCopy()));
                }
            });
        }
        if (AbstractDungeon.miscRng.random(100) < loseChance) {
            flash();
            addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.currentHealth / 4, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            destroy();
        }
    }
}
