package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.effects.MoveToEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.DecayEulogyPower;

public class GnawSting extends BaseMonster {
    public static final String ID = GnawSting.class.getSimpleName();

    int decayCount = 9;

    public GnawSting(float x, float y) {
        super(ID, 200, 218, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? -1 : 1;

        decayCount = specialAs ? 10 : 9;

        addMove(Intent.ATTACK, moreDamageAs ? 9 : 3, mi -> {
            addToBot(new VFXAction(new ClashEffect(p.hb.cX, p.hb.cY)));
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DecayEulogyPower(this, decayCount)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
