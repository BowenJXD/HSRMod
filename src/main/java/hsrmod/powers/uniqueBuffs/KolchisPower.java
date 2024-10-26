package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.PreEnergyChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class KolchisPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(KolchisPower.class.getSimpleName());

    int chargeThreshold = 100;
    
    public KolchisPower(int chargeThreshold) {
        super(POWER_ID);
        this.chargeThreshold = chargeThreshold;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], chargeThreshold);
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

            amount += -stackAmount;
            if (amount >= chargeThreshold) {
                int energyGain = amount / chargeThreshold;
                amount %= chargeThreshold;
                addToBot(new GainEnergyAction(energyGain));
            }
        }
        return stackAmount;
    }
}
