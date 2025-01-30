package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.enemyOnly.RebirthPower;

public class MaraStruckSoldier extends BaseMonster {
    public static final String ID = MaraStruckSoldier.class.getSimpleName();
    
    public MaraStruckSoldier(float x, float y) {
        super(ID, 200, 256, x, y);
        
        addMove(Intent.ATTACK_DEBUFF, 2, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new WindShearPower(p, this, 2)));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new RebirthPower(this, 1)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
