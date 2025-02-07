package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.breaks.WindShearPower;

public class ShadowJackhyena extends BaseMonster {
    public static final String ID = ShadowJackhyena.class.getSimpleName();
    
    int windShearCount = 1;
    
    public ShadowJackhyena(float x, float y) {
        super(ID, 200, 178, x, y);
        
        windShearCount = specialAs ? 2 : 1;
        
        addMove(Intent.ATTACK_DEBUFF, moreDamageAs ? 3 : 2, mi -> {
            addToBot(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY, Color.PURPLE)));
            attack(mi, AbstractGameAction.AttackEffect.NONE, AttackAnim.MOVE);
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, windShearCount)));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
