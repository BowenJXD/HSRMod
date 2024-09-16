package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.NeowsLament;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardRewardPoolEditor;

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
        if (counter <= 0) return;
        if (AbstractDungeon.relicRng.random(100) < winChance) {
            flash();
            CardRewardPoolEditor.getInstance().extraRelics++;
        } else if (AbstractDungeon.relicRng.random(100) < loseChance) {
            flash();
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.currentHealth / 2, DamageInfo.DamageType.HP_LOSS));
            destroy();
        }
    }
}
