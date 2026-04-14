package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.PowerPower;
import hsrmod.utils.GeneralUtil;

public class Trailblazer10Power extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer10Power.class.getSimpleName());
    
    public Trailblazer10Power(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        addToTop(new ApplyPowerAction(owner, owner, new ObsessionPower(owner,  amount)));
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount, 1);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        super.onEvokeOrb(orb);
        addToTop(new DrawCardAction(1));
    }
}
