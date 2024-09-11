package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class VoyageMonitorPower extends PowerPower implements PreBreakDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(VoyageMonitorPower.class.getSimpleName());

    public VoyageMonitorPower() {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BrokenPower) {
            this.flash();
            int amt = ToughnessPower.getStackLimit(target);
            addToBot(new ApplyPowerAction(owner, owner, new ToughnessPower(owner, amt), amt));
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscribeManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscribeManager.getInstance().unsubscribe(this);
    }

    @Override
    public float preBreakDamage(float amount, AbstractCreature target) {
        addToBot(new ApplyPowerAction(owner, owner, new ToughnessPower(owner, 1), 1));
        return amount;
    }
}
