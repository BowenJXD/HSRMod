package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DrainingHitPower;
import hsrmod.utils.ModHelper;

public class ShapeShifter extends BaseMonster {
    public static final String ID = ShapeShifter.class.getSimpleName();
    
    int hpLossPercentage = 20;
    int drainingHitAmt = 5;

    public ShapeShifter(float x, float y){
        super(ID, 340, 384, x, y);
        
        drainingHitAmt = ModHelper.specialAscension(type) ? 6 : 5;
        
        addSlot(-400, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(200, AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = slot -> new MaraStruckSoldier(slot.x, slot.y);
        
        addMoveA(Intent.ATTACK, 5, 2, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.FAST);
        });
        addMove(Intent.BUFF, mi -> {
            shout(0, 1);
            addToBot(new LoseHPAction(this, this, currentHealth * hpLossPercentage / 100));
            spawnMonsters();
            addToBot(new ApplyPowerAction(this, this, new DrainingHitPower(this, drainingHitAmt)));
        });
        addMoveA(Intent.ATTACK, 5, 3, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HEAVY, AttackAnim.SLOW);
        });
    }

    @Override
    protected void getMove(int i) {
        if (ModHelper.specialAscension(type)) i = 1;
        switch (i % 2) {
            case 0:
                setMove(0);
                break;
            case 1:
                if (hasPower(DrainingHitPower.POWER_ID))
                    setMove(2);
                else
                    setMove(1);
                break;
        }
    }
}
