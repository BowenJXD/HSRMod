package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.powers.misc.ToughnessPower;

public class WorldpurgeTail extends BaseMonster {
    public static final String ID = WorldpurgeTail.class.getSimpleName();
    
    public WorldpurgeTail(float x, float y) {
        super(ID, 128, 128, x, y);
        floatIndex = -0.5f;

        addMove(Intent.ATTACK, 6, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        });
        addMoveA(Intent.ATTACK_DEBUFF, 12, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
            if (specialAs)
                addToBot(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
        });
        addMove(Intent.STUN, mi -> {});
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ToughnessPower.POWER_ID) && hasPower(LockToughnessPower.POWER_ID)) {
            setMove(2);
        } else if (hasPower(ResonatePower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
