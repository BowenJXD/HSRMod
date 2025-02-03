package hsrmod.monsters.Exordium;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SnarelockPower;
import hsrmod.utils.ModHelper;

public class DawnsLeftHand extends BaseMonster {
    public static final String ID = DawnsLeftHand.class.getSimpleName();

    public DawnsLeftHand() {
        super(ID, 0F, 0F, 200, 150, 250, 25);
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(5, 12);
        } else {
            setDamages(5, 8);
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, this, new EntanglePower(p, this, 1), 1));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (p.hasPower(EntanglePower.POWER_ID)) {
            setMove(MOVES[2], (byte) 2, Intent.ATTACK, this.damage.get(1).base);
        } else {
            if (lastMove((byte) 0)) {
                setMove(MOVES[1], (byte) 1, Intent.DEBUFF);
            } else {
                setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        this.animY = MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    public void die() {
        super.die();
        if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(ModHelper::check)) {
            AbstractDungeon.getMonsters().monsters.forEach(m -> {
                addToBot(new InstantKillAction(m));
            });
        }
    }
}
