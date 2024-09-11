package hsrmod.powers.uniqueDebuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.SubscribeManager;

public class EpiphanyPower extends DebuffPower implements OnPowersModifiedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(EpiphanyPower.class.getSimpleName());

    public EpiphanyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = true);
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        reducePower(1);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void receivePowersModified() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
    }
}
