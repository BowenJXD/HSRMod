package hsrmod.actions;

import hsrmod.powers.misc.EnergyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReduceChargeByHandCardNumAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    
    private int multiplier = 20;

    public ReduceChargeByHandCardNumAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source, amount);
        this.duration = DURATION;
    }
    
    @Override
    public void update() {
        if (this.duration == DURATION) {
            AbstractPlayer p = (AbstractPlayer)target;
            AbstractPower power = p.getPower(EnergyPower.POWER_ID);
            
            int reduceAmount = 0;
            for (AbstractCard card : p.hand.group) {
                if (card.retain)
                    reduceAmount += multiplier;
            }
            
            if (power != null && !p.hand.isEmpty() && reduceAmount > 0) {
                addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, -reduceAmount), -reduceAmount));
            }
            this.isDone = true;
        }
        this.tickDuration();
    }
}
