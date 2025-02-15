package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;

public class SpringLoader extends BaseMonster {
    public static final String ID = SpringLoader.class.getSimpleName();
    
    public SpringLoader(float x, float y){
        super(ID, 100, 220, x, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;
        
        addMoveA(Intent.ATTACK_DEBUFF, 10, mi->{
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
