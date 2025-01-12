package hsrmod.monsters.Exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.StasisPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;

public class RobotArmUnit extends BaseMonster {
    public static final String ID = RobotArmUnit.class.getSimpleName();
    
    public RobotArmUnit(float x, float y) {
        super(ID, 0F, -15.0F, 230, 197, x, y);
        setDamagesWithAscension(16);
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case -1:
            case 0:
                addToBot(new ApplyStasisAction(this));
                break;
            case 1:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[3], 1)));
                break;
            case 2:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new VFXAction(new ExplosionSmallEffect(hb.cX, hb.cY), 0.1F));
                    addToBot(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                    addToBot(new SuicideAction(this));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    public void update() {
        super.update();
        this.animY = -MathUtils.cosDeg((float)(System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 1)) {
            setMove(MOVES[2], (byte) 2, Intent.ATTACK, damage.get(0).base);
        } else if (!hasPower(StasisPower.POWER_ID)) {
            setMove(MOVES[0], (byte) 0, Intent.UNKNOWN);
        } else {
            setMove(MOVES[1], (byte) 1, Intent.BUFF);
        }
    }
}
