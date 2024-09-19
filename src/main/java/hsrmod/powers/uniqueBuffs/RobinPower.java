package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.EnergyPower;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class RobinPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(RobinPower.class.getSimpleName());

    int strengthGained = 0;
    
    public RobinPower(AbstractCreature owner, boolean upgraded) {
        super(POWER_ID, owner, upgraded);
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
        addToBot(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, strengthGained));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
        if (card.hasTag(FOLLOW_UP)) addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, 20), 20));
    }
}
