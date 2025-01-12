package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class FrigidProwler extends BaseMonster {
    public static final String ID = FrigidProwler.class.getSimpleName();

    public FrigidProwler() {
        super(ID, 0F, -15.0F, 300, 384, 0, 0);
        setDamagesWithAscension(5, 9, 3);
        addSlot(-300.0F, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type)) {
            spawnMonsters();
        }
    }

    private void spawnMonsters() {
        for (int i = 0; i < 2; i++) {
            MonsterSlot slot = getEmptySlot();
            if (slot != null) {
                AbstractMonster monster = new EverwinterShadewalker(slot.x, slot.y);
                monster.usePreBattleAction();
                addToBot(new SpawnMonsterAction(monster, true));
                slot.setMonster(monster);
            }
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                spawnMonsters();
                break;
            case 1:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 3:
                MonsterSlot slot = getOccupiedSlot();
                if (slot != null) {
                    addToBot(new InstantKillAction(slot.monster));
                    addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1)));
                } else {
                    addToBot(new ApplyPowerAction(this, this, new VulnerablePower(this, 1, true)));
                }
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    int handCount = p.hand.size();
                    addToBot(new AnimateJumpAction(this));
                    addDamageActions(p, 2, 3, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                    addToBot(new HealAction(this, this, handCount));
                    addToBot(new ExhaustAction(handCount, true, false, false));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 3) && hasPower(ChargingPower.POWER_ID)) {
            setMove(MOVES[4], (byte) 4, Intent.ATTACK_BUFF, this.damage.get(2).base, 3, true);
        }
        else if ((turnCount == 0 && !ModHelper.specialAscension(type)) || (turnCount > 0 && i < 33 * getEmptySlotCount())) {
            setMove(MOVES[0], (byte) 0, Intent.UNKNOWN);
        }
        else {
            int frozenCount = p.hand.group.stream().mapToInt(c -> c instanceof Frozen ? 1 : 0).sum();
            if (frozenCount == 0) {
                setMove(MOVES[1], (byte) 1, Intent.ATTACK, this.damage.get(0).base);
            }
            else if (frozenCount < 3) {
                setMove(MOVES[2], (byte) 2, Intent.ATTACK, this.damage.get(1).base);
            }
            else {
                setMove(MOVES[3], (byte) 3, Intent.UNKNOWN);
            }
        }
        turnCount++;
    }
}
