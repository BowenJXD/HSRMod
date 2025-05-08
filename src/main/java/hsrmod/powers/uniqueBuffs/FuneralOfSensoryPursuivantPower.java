package hsrmod.powers.uniqueBuffs;

import hsrmod.Hsrmod;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.misc.SuspicionPower;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FuneralOfSensoryPursuivantPower extends PowerPower implements PreDoTDamageSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(FuneralOfSensoryPursuivantPower.class.getSimpleName());
    
    int triggerStack = 3;
    int applyStack = 1;
    
    public FuneralOfSensoryPursuivantPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (upgraded)
            this.description = String.format(DESCRIPTIONS[0], triggerStack);
        else
            this.description = String.format(DESCRIPTIONS[1], triggerStack, applyStack);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (upgraded && power instanceof DoTPower && source == owner) {
            trigger(target, 1);
        }
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) && info.owner == owner)
            trigger(target, 3);
        return info.output;
    }
    
    void trigger(AbstractCreature c, int amt){
        addToBot(new ApplyPowerAction(c, owner, new SuspicionPower(c, amt), amt));
    }
}
