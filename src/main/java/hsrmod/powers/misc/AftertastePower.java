package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Collections;
import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class AftertastePower extends BuffPower implements PreElementalDamageSubscriber {
    public static final String POWER_ID = HSRMod.makePath(AftertastePower.class.getSimpleName());
    
    public AftertastePower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
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
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new ReducePowerAction(owner, owner, this, amount - 1));
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (action.info != null 
                    && action.info.card != null 
                    && action.info.card.hasTag(FOLLOW_UP) 
                    && ModHelper.check(action.target) 
                    && action.target != owner
                    && action.info.owner == owner) {
                flash();
                // addToTop(new ApplyPowerAction(info.owner, info.owner, new EnergyPower(info.owner, -ENERGY_REQUIRED), -ENERGY_REQUIRED));
                addToBot(new ElementalDamageAction(action.target, new ElementalDamageInfo(owner, amount, DamageInfo.DamageType.NORMAL,
                        ModHelper.getRandomEnumValue(ElementType.class), 1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(owner, owner, this, 1));
            }
        }
        return dmg;
    }
}
