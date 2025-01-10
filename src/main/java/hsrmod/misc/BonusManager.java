package hsrmod.misc;

import basemod.BaseMod;
import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.monsters.SequenceTrotter;

public class BonusManager implements OnStartBattleSubscriber {
    private static BonusManager instance;
    
    int appearChance = 10;
    
    private BonusManager() {
        BaseMod.subscribe(this);
    }
    
    public static BonusManager getInstance() {
        if (instance == null) {
            instance = new BonusManager();
        }
        return instance;
    }
    
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (AbstractDungeon.actNum < 3 
                && AbstractDungeon.actNum > 0 
                && !room.eliteTrigger
                && room.monsters.monsters.stream().noneMatch(m -> m instanceof SequenceTrotter || m.type == AbstractMonster.EnemyType.BOSS)
                && AbstractDungeon.monsterRng.random(99) < appearChance) {
            AbstractMonster monster = new SequenceTrotter(400, 0);
            monster.usePreBattleAction();
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(monster, false));
            appearChance /= 2;
        }
    }
}
