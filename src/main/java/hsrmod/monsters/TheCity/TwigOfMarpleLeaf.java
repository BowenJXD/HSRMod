package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public class TwigOfMarpleLeaf extends BaseMonster {
    public static final String ID = TwigOfMarpleLeaf.class.getSimpleName();
    
    int thornCount = 2;
    
    public TwigOfMarpleLeaf(float x, float y) {
        super(ID, 100, 231, x, y);
        
        addMove(Intent.BUFF, mi -> {
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof AbundantEbonDeer).forEach(m -> {
                addToBot(new ApplyPowerAction(m, this, new ThornsPower(m, 1)));
                addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1)));
            });
            if (specialAs) {
                addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, 1)));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
            }
        });
        addMoveA(Intent.ATTACK, 6,  mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, thornCount)));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 0)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
