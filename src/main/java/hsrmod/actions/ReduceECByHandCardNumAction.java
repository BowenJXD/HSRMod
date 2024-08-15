package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.powers.EnergyPower;

public class ReduceECByHandCardNumAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    
    private int multiplier = 10;

    public ReduceECByHandCardNumAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source, amount);
        this.duration = DURATION;
    }
    
    @Override
    public void update() {
        if (this.duration == DURATION) {
            AbstractPlayer p = (AbstractPlayer)target;
            AbstractPower power = p.getPower(EnergyPower.POWER_ID);
            if (power != null && !p.hand.isEmpty()) {
                addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, -p.hand.size() * multiplier), -p.hand.size() * multiplier));
            }
            this.isDone = true;
        }
        this.tickDuration();
    }
}
