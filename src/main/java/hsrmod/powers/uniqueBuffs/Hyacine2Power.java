package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreOrbPassiveOrEvokeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;

public class Hyacine2Power extends PowerPower implements PreOrbPassiveOrEvokeSubscriber {
    public static final String POWER_ID = HSRMod.makePath(Hyacine2Power.class.getSimpleName());
    
    public Hyacine2Power(int amount) {
        super(POWER_ID, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[1], amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        super.onEvokeOrb(orb);
        if (orb.ID.equals(Frost.ORB_ID)) {
            flash();
            addToBot(new AddTemporaryHPAction(owner, owner, orb.evokeAmount));
        }
    }

    @Override
    public int preOrbPassive(AbstractOrb orb, int amountThisTime) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (orb.ID.equals(Frost.ORB_ID)) {
                flash();
                addToBot(new AddTemporaryHPAction(owner, owner, amountThisTime));
            }
        }
        return amountThisTime;
    }
}
