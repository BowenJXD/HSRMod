package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class TutoringPower extends BuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(TutoringPower.class.getSimpleName());

    public TutoringPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        if (owner.hasPower(ChannelPower.POWER_ID)) {
            int channelCount = owner.getPower(ChannelPower.POWER_ID).amount;
            if (channelCount > 0) {
                addToTop(new ApplyPowerAction(owner, owner, new ChannelPower(owner, 0, ChannelPower.ChannelType.OFFCLASS_CLASSROOM), -channelCount));
            }
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}
