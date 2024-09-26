package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.ModHelper;

import java.util.Collections;
import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class AftertastePower extends BuffPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(AftertastePower.class.getSimpleName());

    public static final int ENERGY_REQUIRED = 10;
    
    public AftertastePower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        boolean result = false;
        if (!(o instanceof AbstractCard)) return result;
        if (list.stream().anyMatch(mod -> mod instanceof AftertasteModifier)) return result;
        AbstractCard card = (AbstractCard) o;
        if (card.hasTag(FOLLOW_UP)) {
            return true;
        }

        return result;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new AftertasteModifier());
    }
    
    void trigger(DamageInfo info, AbstractCreature target){
        flash();
        addToTop(new ApplyPowerAction(info.owner, info.owner, new EnergyPower(info.owner, -ENERGY_REQUIRED), -ENERGY_REQUIRED));
        addToBot(new ElementalDamageAction(target, new DamageInfo(info.owner, amount, DamageInfo.DamageType.NORMAL),
                ModHelper.getRandomEnumValue(ElementType.class), 1, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(info.owner, info.owner, this, 1));
    }
    
    public static class AftertasteModifier extends AbstractDamageModifier {

        public AftertasteModifier() {
        }

        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (!target.isDeadOrEscaped()
                    && target.currentHealth > 0  
                    && target != AbstractDungeon.player
                    && info.owner.hasPower(AftertastePower.POWER_ID) 
                    && ModHelper.getPowerCount(info.owner, EnergyPower.POWER_ID) >= AftertastePower.ENERGY_REQUIRED) {
                ((AftertastePower) info.owner.getPower(AftertastePower.POWER_ID)).trigger(info, target);
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new AftertasteModifier();
        }
    }
}
