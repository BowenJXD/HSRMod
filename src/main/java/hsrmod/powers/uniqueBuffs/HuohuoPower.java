package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.CleanAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.interfaces.OnReceivePowerPower;
import hsrmod.powers.misc.EnergyPower;

public class HuohuoPower extends BuffPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(HuohuoPower.class.getSimpleName());
    
    public HuohuoPower(AbstractCreature owner, boolean upgraded) {
        super(POWER_ID, owner, upgraded);
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        remove(1);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        trigger();
    }
    
    void trigger(){
        flash();
        addToBot(new HealAction(owner, owner, 1));
        addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, 10), 10));
        addToBot(new CleanAction(owner, 1, false));
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof EnergyPower
                && stackAmount < 0
                && upgraded) {
            trigger();
        }
        return stackAmount;
    }
}
