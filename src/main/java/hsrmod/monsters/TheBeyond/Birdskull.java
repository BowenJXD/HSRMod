package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;

public class Birdskull extends BaseMonster {
    public static final String ID = Birdskull.class.getSimpleName();
    
    public Birdskull(float x, float y){
        super(ID, 120, 120, x + 50, y);
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;
        
        addMoveA(Intent.ATTACK_DEBUFF, 10, mi->{
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new BleedingPower(p, this, 1)));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
