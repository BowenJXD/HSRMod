package hsrmod.powers.uniqueDebuffs;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class BefogPower extends DebuffPower implements OnReceivePowerPower{
    public static final String POWER_ID = HSRMod.makePath(BefogPower.class.getSimpleName());
    
    public BefogPower(AbstractCreature owner, int Amount, boolean upgraded) {
        super(POWER_ID, owner, Amount, upgraded);
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (upgraded)
            this.description = String.format(DESCRIPTIONS[1], amount, amount);
        else
            this.description = String.format(DESCRIPTIONS[0], amount, amount);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower){
            this.flash();
            addToBot(new GainEnergyAction(amount));
            addToBot(new DrawCardAction(amount));

            if (upgraded)
                ModHelper.addToBotAbstract(() -> {
                    if (AbstractDungeon.player.hand.isEmpty()) return;
                    Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
                    while (hand.hasNext()) {
                        AbstractCard c = hand.next();
                        if (c.canUpgrade()) {
                            addToBot(new UpgradeSpecificCardAction(c));
                        }
                    }
                });

            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        return true;
    }
}
