package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class Stormbringer extends BaseMonster {
    public static final String ID = Stormbringer.class.getSimpleName();
    
    int windStack = 2;
    
    public Stormbringer(float x, float y) {
        super(ID, 0F, -15.0F, 300, 384, x, y);
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(2, 3);
        } else {
            setDamages(2, 4);
        }
        windStack = ModHelper.specialAscension(type) ? 3 : 2;
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addDamageActions(p, 0, 1, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, 2), 2));
                break;
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
                addDamageActions(p, 1, 1, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
                addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, 1), 1));
                break;
            case 2:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, String.format(MOVES[4], windStack), 1)));
                break;
            case 3:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new TriggerDoTAction(p, this, 1, WindShearPower.POWER_ID));
                    addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, windStack), windStack));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int windCount = ModHelper.getPowerCount(p, WindShearPower.POWER_ID);
        if (lastMove((byte) 2) && hasPower(ChargingPower.POWER_ID)) {
            setMove(MOVES[3], (byte) 3, Intent.DEBUFF);
        } else if (windCount > 2) {
            setMove(MOVES[2], (byte) 2, Intent.BUFF);
        } else if (windCount > 0 && i < 50) {
            setMove(MOVES[1], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        } else {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
        turnCount++;
    }
}
