package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.monsters.BaseMonster;

public class EverwinterShadewalker extends BaseMonster {
    public static final String ID = EverwinterShadewalker.class.getSimpleName();
    
    public EverwinterShadewalker(float x, float y) {
        super(ID, 0F, -15.0F, 200, 256, x, y);
        setDamagesWithAscension(5);
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateHopAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
