package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.CleanAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;

public class CorporealFormulaPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(CorporealFormulaPower.class.getSimpleName());
    
    int strengthGain = 1;
    
    public CorporealFormulaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], strengthGain, amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        addToBot(new CleanAction(owner, 99, true));
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthGain)));
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return super.atDamageReceive(damage, damageType) * amount;
    }
}
