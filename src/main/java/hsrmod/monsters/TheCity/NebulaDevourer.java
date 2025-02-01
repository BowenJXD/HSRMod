package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.OutragePower;
import hsrmod.powers.enemyOnly.ResonatePower;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class NebulaDevourer extends BaseMonster {
    public static final String ID = NebulaDevourer.class.getSimpleName();
    
    public NebulaDevourer(float x, float y) {
        super(ID, 200, 256, x, y);
        floatIndex = 0.5f;
        
        addMove(Intent.ATTACK, 4, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        });
        addMove(Intent.ATTACK_DEBUFF, moreDamageAs ? 10 : 8, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            addToBot(new ApplyPowerAction(p, this, new OutragePower(p, 1)));
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
