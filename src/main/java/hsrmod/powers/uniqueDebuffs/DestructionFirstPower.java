package hsrmod.powers.uniqueDebuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class DestructionFirstPower extends DebuffPower implements OnDrawPileShufflePower {
    public static final String POWER_ID = HSRMod.makePath(DestructionFirstPower.class.getSimpleName());

    int magicNumber;

    public DestructionFirstPower(AbstractCreature owner, int damage, int cards) {
        super(POWER_ID, owner, damage);
        this.magicNumber = cards;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount, magicNumber);
    }

    @Override
    public void onShuffle() {
        flash();
        addToBot(new LoseHPAction(owner, owner, amount));
        // addToBot(new ExhaustAction(magicNumber, true, false, false));
    }
}