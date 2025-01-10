package hsrmod.monsters.Exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.breaks.ShockPower;

public class Thunderspawn extends BaseMonster {
    public static final String ID = Thunderspawn.class.getSimpleName();
    
    int direction = 0;
    
    public Thunderspawn(float x, float y) {
        super(ID, 0F, -15.0F, 135, 144, x, y);
        // setDamagesWithAscension(1);
        direction = AbstractDungeon.monsterRng.randomBoolean() ? 1 : -1;
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateSlowAttackAction(this));
        // addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(p, this, new ShockPower(p, this, 1), 1));
    }

    @Override
    public void update() {
        super.update();
        this.animY = direction * MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.DEBUFF);
    }
}
