package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.monsters.BaseMonster;

public class Ballistarius extends BaseMonster {
    public static final String ID = Ballistarius.class.getSimpleName();
    
    int attackCount = 5;
    
    public Ballistarius(float x, float y) {
        super(ID, 150, 256, x, y);
        
        attackCount = moreDamageAs ? 5 : 4;
        
        addMove(Intent.ATTACK_BUFF, 1, attackCount, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
        });
    }
    
    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
