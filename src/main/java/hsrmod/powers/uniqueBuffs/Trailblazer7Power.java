package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreToughnessReduceSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.Collections;
import java.util.List;

public class Trailblazer7Power extends PowerPower implements DamageModApplyingPower, PreToughnessReduceSubscriber {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer7Power.class.getSimpleName());
    
    public int percentage;
    
    public Trailblazer7Power(int percentage) {
        super(POWER_ID);
        this.percentage = percentage;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return o instanceof AbstractCard
                && list.stream().noneMatch(mod -> mod instanceof Trailblazer7DamageMod);
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new Trailblazer7DamageMod(owner, percentage));
    }

    @Override
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (SubscriptionManager.checkSubscriber(this)
                && target != owner
                && target.currentBlock > 0
                && amount > 0) {
            return amount * (1 + percentage / 100.0f);
        }
        return amount;
    }

    public static class Trailblazer7DamageMod extends AbstractDamageModifier {
        AbstractCreature owner;
        int percentage = 0;

        public Trailblazer7DamageMod(AbstractCreature owner, int percentage) {
            this.owner = owner;
            this.percentage = percentage;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            if (target != null 
                    && target != owner
                    && target.currentBlock > 0
                    && damage > 0) {
                return damage * (1 + percentage / 100.0f);
            }
            return damage;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new Trailblazer7DamageMod(owner, percentage);
        }
    }
}
