package hsrmod.misc;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.monsters.SequenceTrotter;
import hsrmod.monsters.WarpTrotter;
import hsrmod.utils.ModHelper;

public class BonusManager implements OnStartBattleSubscriber, StartActSubscriber, CustomSavable<Integer> {
    private static BonusManager instance;
    
    int appearChance = 10;
    int warpChance = 20;
    
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
                && AbstractDungeon.monsterRng.random(99) < appearChance
                && !BaseMod.hasModID("spireTogether:")) {
            AbstractMonster monster = AbstractDungeon.monsterRng.random(99) < warpChance ? new WarpTrotter(400, 0) : new SequenceTrotter(400, 0);
            monster.usePreBattleAction();
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(monster, false));
            appearChance /= 2;
        }
    }

    @Override
    public void receiveStartAct() {
        appearChance = 10;
    }

    @Override
    public Integer onSave() {
        return appearChance;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer == null) return;
        appearChance = integer;
    }
}
