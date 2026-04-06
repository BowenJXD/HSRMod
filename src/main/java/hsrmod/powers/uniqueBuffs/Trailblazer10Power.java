package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.powers.PowerPower;

public class Trailblazer10Power extends PowerPower {
    public static final String POWER_ID = Trailblazer10Power.class.getSimpleName();
    
    public Trailblazer10Power(int amount) {
        super(POWER_ID, amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        super.onEvokeOrb(orb);
        addToTop(new DrawCardAction(1));
    }
}
