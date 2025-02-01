package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class PlaneshredClaws extends BaseMonster {
    public static final String ID = PlaneshredClaws.class.getSimpleName();
    
    public PlaneshredClaws(float x, float y) {
        super(ID, 128, 128, x, y);

        addMove(Intent.ATTACK, 6, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        });
        addMove(Intent.ATTACK, moreDamageAs ? 15 : 12, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            if (specialAs)
                addToBot(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
        });
        addMove(Intent.STUN, mi -> {});
    }

    @Override
    protected void getMove(int i) {
        if (hasPower(ToughnessPower.POWER_ID) && ModHelper.getPowerCount(this, ToughnessPower.POWER_ID) <= 0) {
            setMove(2);
        } else if (hasPower(ResonatePower.POWER_ID)) {
            setMove(1);
        } else {
            setMove(0);
        }
    }
}
