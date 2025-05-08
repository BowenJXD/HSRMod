package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.Hsrmod;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class SlaughterhouseNo4RestInPeacePower extends PowerPower implements PreElementalDamageSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(SlaughterhouseNo4RestInPeacePower.class.getSimpleName());
    
    int percentage;

    public SlaughterhouseNo4RestInPeacePower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], percentage);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this, true);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.info.card != null
                && action.info.owner != null) {
            if ((action.info.card.hasTag(FOLLOW_UP) && upgraded) || action.info.card.followedUp) {
                action.info.tr++;
                if (action.info.owner.hasPower(BrokenPower.POWER_ID)) {
                    if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
                        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                } else if (upgraded) {
                    if (AbstractDungeon.cardRandomRng.random(100) < (100 - percentage)) {
                        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                }
            }
        }
        return dmg;
    }
}
