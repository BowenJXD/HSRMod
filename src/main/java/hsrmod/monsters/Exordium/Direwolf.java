package hsrmod.monsters.Exordium;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class Direwolf extends BaseMonster {
    public static final String ID = Direwolf.class.getSimpleName();

    public Direwolf(float x, float y) {
        super(ID, 0F, -15.0F, 200, 384, x, y);
        setDamagesWithAscension(4, 9, 1);
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(p, this, new BleedingPower(p, this, 1)));
                break;
            case 1:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[3], 1)));
                break;
            case 2:
                if (hasPower(ChargingPower.POWER_ID)) {
                    for (int i = 0; i < 6; i++) {
                        addToBot(new DamageCallbackAction(
                                p,
                                this.damage.get(2),
                                i < 3 ? AbstractGameAction.AttackEffect.SLASH_DIAGONAL : AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                                dmg -> {
                                    if (dmg > 0) {
                                        addToBot(new ApplyPowerAction(p, this, new BleedingPower(p, this, 1)));
                                    }
                                }
                        ));
                    }
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int bleedCount = ModHelper.getPowerCount(p, BleedingPower.POWER_ID);
        if (lastMove((byte) 1)) {
            setMove(MOVES[2], (byte) 2, Intent.ATTACK, this.damage.get(2).base, 6, true);
        } else if (bleedCount == 0) {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            setMove(MOVES[1], (byte) 1, Intent.ATTACK, this.damage.get(1).base);
        }
        turnCount++;
    }
}
