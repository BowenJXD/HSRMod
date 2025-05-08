package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.Hsrmod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.SporePower;

public class SerfOfCalamityPower extends PowerPower {
    public static final String ID = Hsrmod.makePath(SerfOfCalamityPower.class.getSimpleName());

    public int triggerAmount = 3;
    
    public SerfOfCalamityPower(int triggerAmount) {
        super(ID);
        this.triggerAmount = triggerAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], triggerAmount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= triggerAmount) {
            flash();
            int drawAmount = amount / triggerAmount;
            amount -= drawAmount * triggerAmount;
            addToBot(new DrawCardAction(drawAmount));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof SporePower && power.amount < 0) {
            stackPower(-power.amount);
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (target != null)
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new SporePower(target, 1), 1));
    }
}
