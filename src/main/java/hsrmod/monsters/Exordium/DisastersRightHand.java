package hsrmod.monsters.Exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.utils.ModHelper;

public class DisastersRightHand extends BaseMonster {
    public static final String ID = DisastersRightHand.class.getSimpleName();

    public DisastersRightHand() {
        super(ID, 0F, 0F, 200, 150, -250, 45);
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(6, 12);
        } else {
            setDamages(6, 9);
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1), 1));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (p.hasPower(ImprisonPower.POWER_ID)) {
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
