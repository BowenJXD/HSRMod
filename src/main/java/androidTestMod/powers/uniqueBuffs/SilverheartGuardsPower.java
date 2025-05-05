package androidTestMod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import androidTestMod.actions.RandomCardFromDrawPileToHandAction;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;
import androidTestMod.powers.misc.EnergyPower;

public class SilverheartGuardsPower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = AndroidTestMod.makePath(SilverheartGuardsPower.class.getSimpleName());

    int drawAmount = 1;
    int blockAmount = 1;
    
    public SilverheartGuardsPower(int drawAmount, int blockAmount) {
        super(POWER_ID);
        this.drawAmount = drawAmount;
        this.blockAmount = blockAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], drawAmount, blockAmount);
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
            addToBot(new RandomCardFromDrawPileToHandAction());
            addToBot(new GainBlockAction(owner, blockAmount));
        }
        return stackAmount;
    }
}
