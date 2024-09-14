package hsrmod.powers.uniqueDebuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class BefogPower extends DebuffPower implements OnReceivePowerPower{
    public static final String POWER_ID = HSRMod.makePath(BefogPower.class.getSimpleName());

    int drawNum = 0;
    int energyNum = 0;
    
    public BefogPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.drawNum = Amount;
        this.energyNum = Amount;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], drawNum, energyNum);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower){
            this.flash();
            addToBot(new GainEnergyAction(energyNum));
            addToBot(new DrawCardAction(drawNum));

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

            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
        return true;
    }
}
