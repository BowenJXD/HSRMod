package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class BrokenPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(BrokenPower.class.getSimpleName());

    private float damageIncrementPercentage = 1f / 2f; 
    
    private float damageDecrementPercentage = 1f / 4f;
    
    public boolean doReduce = false;
    
    public BrokenPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        priority = 6;
        amount = -1;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], Math.round(damageIncrementPercentage * 100),
                Math.round(damageDecrementPercentage * 100));
    }
    
    @Override
    public void onInitialApplication() {
        this.type = PowerType.DEBUFF;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return Math.round(damage * (1 + damageIncrementPercentage));
        }
        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return Math.round(damage * (1 - damageDecrementPercentage));
        }
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        doReduce = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (doReduce) {
            doReduce = false;
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
