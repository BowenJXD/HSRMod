package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
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
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (!available) return;
        if (AbstractDungeon.relicRng.random(100) < winChance) {
            flash();
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), 
                    RelicLibrary.getRelic(AbstractDungeon.returnRandomRelicKey(AbstractDungeon.returnRandomRelicTier())).makeCopy());
        } else if (AbstractDungeon.relicRng.random(100) < loseChance) {
            flash();
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.currentHealth / 2, DamageInfo.DamageType.HP_LOSS));
            available = false;
            counter = 0;
        }
    }
}
