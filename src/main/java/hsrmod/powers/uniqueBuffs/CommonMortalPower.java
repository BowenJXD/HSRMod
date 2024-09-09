package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

import java.util.List;

public class CommonMortalPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(CommonMortalPower.class.getSimpleName());
    
    int energyAmount = 0;
    
    boolean canTrigger = false;
    
    public CommonMortalPower(int Amount, boolean upgraded) {
        super(POWER_ID, upgraded);
        energyAmount = Amount;
        
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], energyAmount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        canTrigger = true;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!canTrigger) return;
        if ((upgraded && power.type == PowerType.DEBUFF)
                || (!upgraded && power instanceof DoTPower)) {
            canTrigger = false;
            flash();
            trigger();
        }
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        canTrigger = false;
    }

    void trigger(){
        addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, energyAmount), energyAmount));
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof EnergyPower 
                && stackAmount < 0) {
            flash();
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(m, owner, DoTPower.getRandomDoTPower(m, owner, 1), 1));
        }
        return stackAmount;
    }
}
