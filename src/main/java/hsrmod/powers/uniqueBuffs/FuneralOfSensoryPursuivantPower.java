package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.misc.SuspicionPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscribeManager;

public class FuneralOfSensoryPursuivantPower extends PowerPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(FuneralOfSensoryPursuivantPower.class.getSimpleName());
    
    public FuneralOfSensoryPursuivantPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
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
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (upgraded && power instanceof DoTPower) {
            trigger(target);
        }
    }

    @Override
    public float preDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        if (SubscribeManager.checkSubscriber(this))
            trigger(target);
        return amount;
    }
    
    void trigger(AbstractCreature c){
        addToBot(new ApplyPowerAction(c, owner, new SuspicionPower(c, 1), 1));
    }
}
