package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.cardsV2.Abundance.TimeWaitsForNoOne;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.DewDropPower;

public class TimeWaitsForNoOnePower extends StatePower implements InvisiblePower{
    public static final String POWER_ID = HSRMod.makePath(TimeWaitsForNoOnePower.class.getSimpleName());
    
    public TimeWaitsForNoOnePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public int onHeal(int healAmount) {
        addToBot(new ApplyPowerAction(owner, owner, new DewDropPower(owner, amount)));
        return healAmount + amount;
    }

    @Override
    public int onPlayerGainedBlock(int blockAmount) {
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return super.modifyBlock(blockAmount, card);
    }
}
