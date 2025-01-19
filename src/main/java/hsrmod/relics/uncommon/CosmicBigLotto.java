package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;

public class CosmicBigLotto extends BaseRelic {
    public static final String ID = CosmicBigLotto.class.getSimpleName();

    int winChance = 20;
    int loseChance = 10;

    public CosmicBigLotto() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        counter = 1;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (usedUp) return;
        if (AbstractDungeon.relicRng.random(100) < winChance) {
            flash();
            RewardItem rewardItem = new RewardItem(RelicLibrary.getRelic(AbstractDungeon.returnRandomRelicKey(AbstractDungeon.returnRandomRelicTier())).makeCopy());
            AbstractDungeon.combatRewardScreen.rewards.add(rewardItem);
        }
        if (AbstractDungeon.relicRng.random(100) < loseChance) {
            flash();
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.currentHealth / 2, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            destroy();
        }
    }
}
