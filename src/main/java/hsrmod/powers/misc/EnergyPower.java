package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;

public class EnergyPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(EnergyPower.class.getSimpleName());
    
    public static final int AMOUNT_LIMIT = 240;

    public EnergyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], AMOUNT_LIMIT);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);

        // Ensure the new value does not exceed bounds
        if (amount > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        } else if (amount < 0) {
            amount = 0;
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        
        // Ensure the new value does not exceed bounds
        if (amount > AMOUNT_LIMIT) {
            amount = AMOUNT_LIMIT;
        } else if (amount < 0) {
            amount = 0;
        }
    }
}
