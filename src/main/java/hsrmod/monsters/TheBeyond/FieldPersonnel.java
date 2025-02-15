package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.PerformancePointPower;

public class FieldPersonnel extends BaseMonster {
    public static final String ID = FieldPersonnel.class.getSimpleName();

    public FieldPersonnel(float x, float y) {
        super(ID, 150, 256, x, y);
        addMoveA(Intent.ATTACK_BUFF, 3, 2, mi->{
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(this, this, new PerformancePointPower(this, 1)));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new PerformancePointPower(this, 1)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
