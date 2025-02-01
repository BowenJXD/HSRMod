package hsrmod.monsters.TheCity;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.monsters.BaseMonster;

public class MaraStruckWarden extends BaseMonster {
    public static final String ID = MaraStruckWarden.class.getSimpleName();
    
    public MaraStruckWarden(float x, float y) {
        super(ID, 140, 256, x, y);
        
        addMove(Intent.ATTACK_DEBUFF, 3, 2, mi -> {
            addToBot(new AnimateFastAttackAction(this));
            addToBot(new DamageAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_VERTICAL, dmg -> {
                if (dmg > 0) {
                    if (specialAs)
                        addToTop(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
                    else
                        addToTop(new MakeTempCardInDiscardAction(new Wound(), 1));
                }
            }));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
