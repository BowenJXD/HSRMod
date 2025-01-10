package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class SilvermaneLieutenant extends BaseMonster implements PreBreakSubscriber {
    public static final String ID = SilvermaneLieutenant.class.getSimpleName();
    
    int block = 10;
    int thornCount = 3;
    
    public SilvermaneLieutenant(int x, int y) {
        super(ID, 0F, -15.0F, 300, 384, x, y);
        setDamagesWithAscension(7);
        block = ModHelper.moreHPAscension(type) ? 10 : 8;
        thornCount = ModHelper.specialAscension(type) ? 3 : 2;
        addSlot(x - 300, 0);
        addSlot(x + 300, 0);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void takeTurn() {
        if (hasPower(ThornsPower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(this, this, ThornsPower.POWER_ID));
        }
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addDamageActions(p, 0, 1, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                break;
            case 1:
                for (MonsterSlot slot : slots) {
                    if (slot.isEmpty()) {
                        AbstractMonster monster = new SilvermaneSoldier(slot.x, slot.y);
                        monster.usePreBattleAction();
                        addToBot(new SpawnMonsterAction(monster, true));
                        slot.setMonster(monster);
                    }
                }
                break;
            case 2:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (ModHelper.checkMonster(m)) {
                        addToBot(new GainBlockAction(m, this, block));
                    }
                }
                AbstractPower power = new ThornsPower(this, thornCount);
                power.description = String.format(MOVES[3], thornCount);
                addToBot(new ApplyPowerAction(this, this, power));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int soldierCount = getEmptySlotCount();
        if (i < soldierCount * 33) {
            setMove(MOVES[1], (byte) 1, Intent.UNKNOWN);
        }
        else {
            if (i % 2 < 1 || lastMove((byte) 2)) {
                setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
            }
            else {
                setMove(MOVES[2], (byte) 2, Intent.DEFEND_BUFF);
            }
        }
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == this) {
            addToBot(new RemoveSpecificPowerAction(this, this, ThornsPower.POWER_ID));
        }
    }
    
    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
