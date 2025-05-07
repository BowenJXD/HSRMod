package hsrmod.misc;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.Bonus.KingTrashcan;
import hsrmod.monsters.Bonus.LordlyTrashcan;
import hsrmod.monsters.Bonus.SequenceTrotter;
import hsrmod.monsters.Bonus.WarpTrotter;
import hsrmod.subscribers.SubscriptionManager;

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
        if (AbstractDungeon.actNum > 0
                && !room.eliteTrigger
                && room.monsters.monsters.stream().noneMatch(
                        m -> m instanceof SequenceTrotter
                        || m instanceof WarpTrotter
                        || m instanceof KingTrashcan
                        || m instanceof LordlyTrashcan
                        || m.type == AbstractMonster.EnemyType.BOSS
                        || m.type == AbstractMonster.EnemyType.ELITE)
                && (AbstractDungeon.id.contains(HSRMod.MOD_NAME) || AbstractDungeon.player instanceof IHSRCharacter)) {
            float aChance = SubscriptionManager.getInstance().triggerNumChanger(SubscriptionManager.NumChangerType.TROTTER_WEIGHT, appearChance);
            float wChance = SubscriptionManager.getInstance().triggerNumChanger(SubscriptionManager.NumChangerType.TROTTER_WEIGHT, warpChance);
            if (AbstractDungeon.monsterRng.random(99) < aChance) {
                AbstractMonster monster;
                if (AbstractDungeon.actNum < 3)
                    monster = AbstractDungeon.monsterRng.random(99) < wChance ? new WarpTrotter(400, 0) : new SequenceTrotter(400, 0);
                else
                    monster = AbstractDungeon.monsterRng.random(99) < wChance ? new KingTrashcan(400, 0) : new LordlyTrashcan(400, 0);
                monster.usePreBattleAction();
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(monster, false));
                appearChance /= 2;
            }
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
