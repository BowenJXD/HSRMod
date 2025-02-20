package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.effects.MoveToEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.DecayEulogyPower;
import hsrmod.powers.enemyOnly.OutragePower;

public class LesserSting extends BaseMonster {
    public static final String ID = LesserSting.class.getSimpleName();
    
    int decayCount = 9;
    
    public LesserSting(float x, float y) {
        super(ID, 200, 218, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? -1 : 1;
        
        decayCount = specialAs ? 10 : 9;
        
        addMove(Intent.BUFF, mi->{
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        addMove(Intent.ATTACK, 3, 2, mi->{
            addToBot(new VFXAction(new MoveToEffect(this, p.hb.cX + p.hb.width / 2 - hb.cX, p.hb.cY - hb.cY, false, 0.4f)));
            addToBot(new VFXAction(new ExplosionSmallEffect(p.hb.cX + 50, p.hb.cY)));
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            addToBot(new ApplyPowerAction(p, this, new OutragePower(p, 2)));
            addToBot(new MakeTempCardInDrawPileAction(new Slimed(), 1, true, true));
            addToBot(new RemoveSpecificPowerAction(this, this, DecayEulogyPower.POWER_ID));
            addToBot(new SuicideAction(this));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DecayEulogyPower(this, decayCount)));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
