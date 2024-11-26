package hsrmod.powers.misc;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

import java.util.ArrayList;
import java.util.List;

public class ToughnessPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ToughnessPower.class.getSimpleName());
    
    List<Object> lockers;
    boolean locked = false;
    int stackLimit = 0;
    
    public ToughnessPower(AbstractCreature owner, int Amount, int stackLimit) {
        super(POWER_ID, owner, Amount);
        this.priority = 20;
        this.stackLimit = stackLimit;
        canGoNegative = true;
        
        this.updateDescription();
    }
    
    public ToughnessPower(AbstractCreature owner, int Amount) {
        this(owner, Amount, getStackLimit(owner));
    }
    
    public ToughnessPower(AbstractCreature owner) {
        this(owner, getStackLimit(owner));
    }

    @Override
    public void updateDescription() {
        if (amount > 0)
            this.description = String.format(DESCRIPTIONS[0], this.amount, stackLimit);
        else
            this.description = String.format(DESCRIPTIONS[1], -this.amount, stackLimit);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage * ( 1 - (this.amount / 100.0F) );
        return damage;
    }

    @Override
    public void stackPower(int stackAmount) {        
        if (stackAmount == stackLimit && owner != AbstractDungeon.player && BaseMod.hasModID("spireTogether:")) return;
        if (locked) return;
        alterPower(stackAmount);
    }
    
    public void alterPower(int i) {
        this.fontScale = 8.0F;
        this.amount += i;
        
        if (this.amount < -stackLimit) {
            this.amount = -stackLimit;
        }
        else if (this.amount > stackLimit) {
            this.amount = stackLimit;
        }

        // addToTop(new TalkAction(owner, String.format("Amount before: %d, Amount: %d, stack limit: %d!", temp, stackAmount, stackLimit), 1.0F, 2.0F));
        type = this.amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }
    
    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }
    
    public boolean getLocked() {
        return locked;
    }
    
    public void lock(Object locker) {
        if (lockers == null) lockers = new ArrayList<>();
        lockers.add(locker);
        locked = true;
    }
    
    public void unlock(Object locker) {
        if (lockers != null) lockers.remove(locker);
        if (lockers == null || lockers.isEmpty()) locked = false;
    }

    public static int getStackLimit(AbstractCreature c){
        int result = 0;
        if (c.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower power = (ToughnessPower)c.getPower(ToughnessPower.POWER_ID);
            if (power != null) result = power.stackLimit;
        }
        else if (c instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)c;
            switch (m.type) {
                case NORMAL:
                    result = 4;
                    break;
                case ELITE:
                    result = 6;
                    break;
                case BOSS:
                    result = 10;
                    break;
            }
            result *= Math.min(AbstractDungeon.actNum, 4);
            if (BaseMod.hasModID("spireTogether:")) {
                result = Math.round((float)result * 1.5f);
            }
        }
        else if (c instanceof AbstractPlayer) {
            result = 50;
        }
        return result;
    }
}
