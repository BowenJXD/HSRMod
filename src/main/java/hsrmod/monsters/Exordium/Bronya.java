package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.cards.uncommon.Bronya1;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.MultiMovePower;
import hsrmod.utils.ModHelper;

public class Bronya extends BaseMonster {
    public static final String ID = Bronya.class.getSimpleName();

    int bronyaCount = 4;
    
    public Bronya() {
        super(ID, 240, 384, -100, 0);
        setDamagesWithAscension(9, 15);
        addSlot(-300, 30);
        addSlot(100, 30);
        bronyaCount = ModHelper.specialAscension(type) ? 3 : 4;
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                for (MonsterSlot slot : slots) {
                    if (slot.isEmpty()) {
                        AbstractMonster monster = new SilvermaneSoldier(slot.x, slot.y);
                        monster.usePreBattleAction();
                        addToBot(new SpawnMonsterAction(monster, true));
                        slot.setMonster(monster);
                    }
                }
                break;
            case 1:
                // addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, this, new NoDrawPower(p)));
                for (int i = 0; i < bronyaCount; i++) {
                    Bronya1 card = new Bronya1();
                    card.setCostForTurn(0);
                    card.exhaust = true;
                    addToBot(new MakeTempCardInHandAction(card));
                }
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1)));
                break;
            case 3:
                if (hasPower(ChargingPower.POWER_ID)) {
                    int strengthCount = ModHelper.getPowerCount(p, StrengthPower.POWER_ID);
                    addToBot(new RemoveSpecificPowerAction(p, this, StrengthPower.POWER_ID));
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount), strengthCount));
                    addToBot(new DamageAction(p, damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int slotCount = getEmptySlotCount();
        if (lastMove((byte) 2)) {
            setMove(MOVES[3], (byte) 3, Intent.ATTACK_DEBUFF, damage.get(1).base);
        } else if (i < slotCount * 50) {
            setMove(MOVES[0], (byte) 0, Intent.UNKNOWN);
        } else if (i % 2 == 0 && !lastMove((byte) 2)) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m instanceof SilvermaneSoldier && ModHelper.check(m)) {
                    addToBot(new ApplyPowerAction(m, this, new MultiMovePower(m, 1)));
                }
            }
            setMove(MOVES[2], (byte) 2, Intent.DEBUFF);
        } else {
            setMove(MOVES[1], (byte) 1, Intent.ATTACK, damage.get(0).base);
        }
    }
}
