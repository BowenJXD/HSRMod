package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;

public class LanceOfTheEternalFreeze extends BaseMonster {
    public static final String ID = LanceOfTheEternalFreeze.class.getSimpleName();
    
    public LanceOfTheEternalFreeze(float x, float y) {
        this(x, y, 1);
    }

    public LanceOfTheEternalFreeze(float x, float y, int picIndex) {
        super(ID, PathDefine.MONSTER_PATH + ID + "_" + picIndex + ".png", 0.0F, -15.0F, 188F, 243.0F, x, y);

        if (ModHelper.moreDamageAscension(type)) {
            setDamages(6);
        }
        else {
            setDamages(4);
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SummonedPower(this)));
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], false, () ->
                addToTop(new ExhaustAction(1, true, false, false))
        )));
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK, this.damage.get(0).base);
    }
}
