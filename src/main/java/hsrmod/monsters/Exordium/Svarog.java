package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.powers.*;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class Svarog extends BaseMonster {
    public static final String ID = Svarog.class.getSimpleName();

    public Svarog() {
        super(ID, 251, 384, 0, 0);
        setDamagesWithAscension(6, 10);
        addSlot(-300, 0);
        bgm = "Godfather";
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(p, this, new DexterityPower(p, -1), -1));
                break;
            case 1:
                MonsterSlot slot = getEmptySlot();
                if (slot != null) {
                    AbstractMonster monster = new RobotArmUnit(slot.x, slot.y);
                    monster.usePreBattleAction();
                    addToBot(new SpawnMonsterAction(monster, true));
                    slot.setMonster(monster);
                    if (ModHelper.specialAscension(type)) {
                        AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(monster));
                    }
                }
                break;
            case 2:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, 2, true)));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 0) && getEmptySlot() != null) {
            setMove(MOVES[1], (byte) 1, Intent.UNKNOWN);
        } else if (lastMove((byte) 1)) {
            setMove(MOVES[2], (byte) 2, Intent.ATTACK_DEBUFF, damage.get(1).base);
        } else {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, damage.get(0).base);
        }
    }
}
