package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.CorrosionPower;

public class TideErodedBlade extends BaseMonster {
    public static final String ID = TideErodedBlade.class.getSimpleName();

    static final int ERODE_AMT = 5;

    public TideErodedBlade(float x, float y) {
        super(ID, 200, 256, x, y);
        addMoveA(Intent.ATTACK_DEBUFF, 5, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new LoseHPAction(p, this, ERODE_AMT));
            addToBot(new ApplyPowerAction(p, this, new CorrosionPower(p, ERODE_AMT), ERODE_AMT));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
