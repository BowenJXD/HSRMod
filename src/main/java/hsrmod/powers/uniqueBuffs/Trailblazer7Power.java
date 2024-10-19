package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import java.util.Collections;
import java.util.List;

public class Trailblazer7Power extends PowerPower implements DamageModApplyingPower {
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
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return o instanceof AbstractCard
                && list.stream().noneMatch(mod -> mod instanceof Trailblazer7DamageMod);
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new Trailblazer7DamageMod(owner, percentage));
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
                    && damage > 0
                    && target.currentBlock <= owner.currentBlock) {
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
