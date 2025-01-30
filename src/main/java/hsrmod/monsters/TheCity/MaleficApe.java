package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.GustoPower;
import hsrmod.utils.ModHelper;

public class MaleficApe extends BaseMonster {
    public static final String ID = MaleficApe.class.getSimpleName();
    
    int strengthCount = 2;
    int gustoCount = 3;
    
    public MaleficApe(float x, float y) {
        super(ID, 340, 410, x, y);
        
        strengthCount = specialAs ? 3 : 2;
        
        addMove(Intent.BUFF, mi -> {
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount)));
            addToBot(new ApplyPowerAction(this, this, new GustoPower(this, gustoCount)));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        
        addMoveA(Intent.ATTACK_BUFF, 8, mi -> {
            if (hasPower(ChargingPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strengthCount - 1)));
                attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AttackAnim.JUMP);
                addToBot(new ReducePowerAction(this, this, GustoPower.POWER_ID, 1));
                ModHelper.addToBotAbstract(() -> {
                    if (hasPower(GustoPower.POWER_ID)) {
                        addToTop(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
                    }
                });
            }
        });
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ChargingPower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
