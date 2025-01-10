package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class GaugeRecollectionPower extends StatePower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(GaugeRecollectionPower.class.getSimpleName());
    
    boolean removed = false;
    
    public GaugeRecollectionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
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
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            remove(1);
            if (this.amount <= 1) {
                addToTop(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 1, owner instanceof AbstractMonster)));
                removed = true;
            }
        }
        return damageAmount;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) 
                && target == owner 
                && !removed) {
            remove(amount);
            addToTop(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 1, owner instanceof AbstractMonster)));
        }
    }
}
