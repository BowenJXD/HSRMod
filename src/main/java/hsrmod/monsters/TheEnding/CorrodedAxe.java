package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.effects.TopWarningEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BitterFracturePower;
import hsrmod.powers.enemyOnly.CorrosionPower;

public class CorrodedAxe extends BaseMonster {
    public static final String ID = CorrodedAxe.class.getSimpleName();

    static final int ERODE_AMT = 5;

    public CorrodedAxe(float x, float y) {
        super(ID, 200, 256, x, y);
        addMoveA(Intent.ATTACK_DEBUFF, 10, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new LoseHPAction(p, this, ERODE_AMT));
            addToBot(new ApplyPowerAction(p, this, new CorrosionPower(p, ERODE_AMT), ERODE_AMT));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new BitterFracturePower(this, ()  -> {
            AbstractDungeon.topLevelEffects.add(new TopWarningEffect(DIALOG[0]));
        }), 0));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
