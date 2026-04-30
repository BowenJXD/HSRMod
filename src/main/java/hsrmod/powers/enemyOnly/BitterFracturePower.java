package hsrmod.powers.enemyOnly;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PostMonsterDeathSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GAMManager;
import hsrmod.utils.ModHelper;
import org.apache.logging.log4j.Level;

public class BitterFracturePower extends BuffPower implements PostMonsterDeathSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BitterFracturePower.class.getSimpleName());

    public BitterFracturePower(AbstractCreature owner) {
        super(POWER_ID, owner);
        this.amount = -1;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void postMonsterDeath(AbstractMonster monster) {
        if (!SubscriptionManager.checkSubscriber(this)) return;
        if (ModHelper.check(monster)) return;

        AbstractMonster m = (AbstractMonster) owner;
        flash();
        ModHelper.addToBotAbstract(() -> {
            if (!ModHelper.check(m)) return;
            if (!m.hasPower("Barricade")) {
                m.loseBlock();
            }
            m.applyStartOfTurnPowers();
            AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(m));
            GAMManager.addParallelAction(POWER_ID, action -> {
                if (action instanceof RollMoveAction) {
                    try {
                        if (ModHelper.check(m)) {
                            ModHelper.addToTopAbstract(m::createIntent);
                            return true;
                        }
                    } catch (Exception e) {
                        HSRMod.logger.log(Level.WARN, e);
                    }
                }
                return false;
            });
        });
    }
}
