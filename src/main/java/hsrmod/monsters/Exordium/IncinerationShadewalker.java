package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import hsrmod.monsters.BaseMonster;

public class IncinerationShadewalker extends BaseMonster {
    public static final String ID = IncinerationShadewalker.class.getSimpleName();
    
    public IncinerationShadewalker(int x, int y) {
        super(ID, 0F, -15.0F, 200, 256, x, y);
        setDamagesWithAscension(5);
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateHopAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
        addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, true));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
