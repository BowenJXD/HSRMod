package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.SafeguardPower;

public class Heartbreaker extends BaseMonster {
    public static final String ID = Heartbreaker.class.getSimpleName();
    
    public Heartbreaker(float x, float y){
        super(ID, 200, 256, x, y);
        
        addMoveA(Intent.ATTACK, 15, mi->{
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SafeguardPower(this, 2)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
