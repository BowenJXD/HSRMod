package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class SilvermaneSoldier extends BaseMonster {
    public static final String ID = SilvermaneSoldier.class.getSimpleName();
    
    public SilvermaneSoldier(float x, float y) {
        super(ID, 0F, -15.0F, 200, 256, x, y);
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(4, 2);
        } else {
            setDamages(3, 2);
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addDamageActions(p, 0, 1, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                break;
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
                addDamageActions(p, 1, 1, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, 1, true)));
                break;
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (turnCount == 0 && ModHelper.specialAscension(type)) {
            i = 1;
        }
        switch (i % 2) {
            case 0:
                setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
        }
        turnCount++;
    }
}
