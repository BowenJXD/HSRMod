package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, windShearCount)));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
