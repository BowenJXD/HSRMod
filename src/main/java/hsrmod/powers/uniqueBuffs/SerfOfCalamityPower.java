package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.SporePower;
import hsrmod.subscribers.SubscriptionManager;

import java.util.Collections;
import java.util.List;

public class SerfOfCalamityPower extends PowerPower implements BetterOnApplyPowerPower, DamageModApplyingPower {
    public static final String ID = HSRMod.makePath(SerfOfCalamityPower.class.getSimpleName());

    public int triggerAmount = 3;
    
    public SerfOfCalamityPower(int triggerAmount) {
        super(ID);
        this.triggerAmount = triggerAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], triggerAmount);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= triggerAmount) {
            flash();
            int drawAmount = amount / triggerAmount;
            amount -= drawAmount * triggerAmount;
            addToBot(new DrawCardAction(drawAmount));
        }
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int betterOnApplyPowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power instanceof SporePower && power.amount < 0) {
            stackPower(-stackAmount);
        }
        return stackAmount;
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        if (!(o instanceof AbstractCard)) return false;
        if (list.stream().anyMatch(mod -> mod instanceof SerfOfCalamityModifier)) return false;
        return true;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new SerfOfCalamityModifier());
    }

    public static class SerfOfCalamityModifier extends AbstractDamageModifier {
        public SerfOfCalamityModifier() {
            super();
        }

        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (lastDamageTaken > 0
                    && target.currentHealth > 0
                    && target != AbstractDungeon.player){
                addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new SporePower(target, 1), 1));
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SerfOfCalamityModifier();
        }
    }
}
