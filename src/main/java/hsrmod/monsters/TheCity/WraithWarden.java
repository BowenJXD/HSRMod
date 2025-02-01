package hsrmod.monsters.TheCity;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.RebirthPower;

public class WraithWarden extends BaseMonster {
    public static final String ID = WraithWarden.class.getSimpleName();
    
    public WraithWarden(float x, float y) {
        super(ID, 200, 256, x, y);
        
        addMove(Intent.ATTACK_DEBUFF, 4, 2, mi -> {
            addToBot(new AnimateFastAttackAction(this));
            addToBot(new DamageAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            addToBot(new DamageCallbackAction(p, damage.get(mi.index), AbstractGameAction.AttackEffect.SLASH_VERTICAL, dmg -> {
                if (dmg > 0) {
                    addToTop(new MakeTempCardInDrawPileAction(new Dazed(), 1, !specialAs, true));
                }
            }));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
}
