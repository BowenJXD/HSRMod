package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.WarArmorPower;

public class FuriaeWarrior extends BaseMonster {
    public static final String ID = FuriaeWarrior.class.getSimpleName();
    
    int warArmorAmt = specialAs ? 3 : 2;

    public FuriaeWarrior(float x, float y) {
        super(ID, 200, 256, x, y);
        addMoveA(Intent.ATTACK_DEFEND, 10, mi -> {
            addToBot(new GainBlockAction(this, this, 10));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AttackAnim.SLOW);
            addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new WarArmorPower(this, warArmorAmt)));
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
