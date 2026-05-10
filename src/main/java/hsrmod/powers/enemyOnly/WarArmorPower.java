package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class WarArmorPower extends BuffPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(WarArmorPower.class.getSimpleName());

    int dmgReceive = 1;
    int hpLoss = 10;
    int chargeGain = 100;
    
    public WarArmorPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], dmgReceive, hpLoss, chargeGain);
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
        addToBot(new LoseHPAction(owner, owner, hpLoss));
        addToBot(new GainEnergyAction(1));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, owner, new EnergyPower(AbstractDungeon.player, chargeGain), chargeGain));
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS && damage > dmgReceive) {
            return dmgReceive;
        }
        return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > dmgReceive) {
            return dmgReceive;
        }
        return damageAmount;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS) {
            flash();
            remove(1);
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            flash();
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}