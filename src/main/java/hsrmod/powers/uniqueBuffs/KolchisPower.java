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

public class KolchisPower extends PowerPower implements OnReceivePowerPower, PreEnergyChangeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(KolchisPower.class.getSimpleName());

    int chargeThreshold = 100;
    int energyThreshold = 5;
    
    public int amount2 = 0;
    public boolean canGoNegative2 = false;
    protected Color redColor2 = Color.RED.cpy();
    protected Color greenColor2 = Color.GREEN.cpy();
    
    public KolchisPower(int energyThreshold) {
        super(POWER_ID);
        this.energyThreshold = energyThreshold;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], chargeThreshold, this.energyThreshold);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (this.amount2 > 0) {
            if (!this.isTurnBased) {
                this.greenColor2.a = c.a;
                c = this.greenColor2;
            }

            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15.0F * Settings.scale, this.fontScale, c);
        } else if (this.amount2 < 0 && this.canGoNegative2) {
            this.redColor2.a = c.a;
            c = this.redColor2;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15.0F * Settings.scale, this.fontScale, c);
        }
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
            amount2 += -stackAmount;
            if (amount2 >= chargeThreshold) {
                amount2 -= chargeThreshold;
                addToBot(new GainEnergyAction(1));
            }
        }
        return stackAmount;
    }

    @Override
    public int preEnergyChange(int changeAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && changeAmount < 0) {
            flash();
            amount += -changeAmount;
            if (amount >= energyThreshold) {
                amount -= energyThreshold;
                addToBot(new ApplyPowerAction(owner, owner, new BrainInAVatPower(owner, 1), 1));
            }
        }
        return changeAmount;
    }
}
