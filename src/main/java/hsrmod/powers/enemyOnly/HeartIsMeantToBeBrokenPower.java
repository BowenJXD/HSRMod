package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

// TODO: add audio and bubble text
public class HeartIsMeantToBeBrokenPower extends DebuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(HeartIsMeantToBeBrokenPower.class.getSimpleName());
    
    public boolean canTrigger = true;
    
    public HeartIsMeantToBeBrokenPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        priority = 100;
        this.updateDescription();
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
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (ModHelper.getPowerCount(owner, InvinciblePower.POWER_ID) <= 0) {
            if (canTrigger){
                addToTop(new ElementalDamageAction(owner, new ElementalDamageInfo(owner, 0, null, ToughnessPower.getStackLimit(owner)), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                canTrigger = false;
            }
        }
        else {
            canTrigger = true;
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && info.elementType != null && info.owner == AbstractDungeon.player) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToTop(new RemoveSpecificPowerAction(owner, owner, InvinciblePower.POWER_ID));
            addToTop(new BreakDamageAction(target, new DamageInfo(owner, ToughnessPower.getStackLimit(owner)), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        }
    }
}
