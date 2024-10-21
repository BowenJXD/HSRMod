package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class ToughnessPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(ToughnessPower.class.getSimpleName());
    
    int stackLimit = 0;
    
    public ToughnessPower(AbstractCreature owner, int Amount, int stackLimit) {
        super(POWER_ID, owner, Amount);
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
        if (stackAmount == stackLimit && owner != AbstractDungeon.player) return;
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

    public static int getStackLimit(AbstractCreature c){
        int result = 0;
        if (c instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)c;
            switch (m.type) {
                case NORMAL:
                    result = 3;
                    break;
                case ELITE:
                    result = 5;
                    break;
                case BOSS:
                    result = 10;
                    break;
            }
            result += Math.min(AbstractDungeon.actNum, 4) * result;
        }
        else if (c instanceof AbstractPlayer) {
            result = 50;
        }
        return result;
    }
}
