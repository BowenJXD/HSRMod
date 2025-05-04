package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import hsrmod.relics.BaseRelic;

public class TowatCards extends BaseRelic {
    public static final String ID = TowatCards.class.getSimpleName();
    
    public TowatCards() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && !usedUp) {
            destroy();
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 2)));
        }
    }
}
