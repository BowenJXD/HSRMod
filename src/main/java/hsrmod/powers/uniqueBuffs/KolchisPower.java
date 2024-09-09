package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.EnergyPower;

public class KolchisPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(KolchisPower.class.getSimpleName());

    int recharge;
    
    public KolchisPower(int recharge) {
        super(POWER_ID);
        this.recharge = recharge;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.recharge);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        int energyChange = card.energyOnUse - EnergyPanel.getCurrentEnergy();
        if (energyChange > 0) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, recharge)));
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
            addToBot(new GainEnergyAction(1));
        }
        return stackAmount;
    }
}
