package hsrmod.powers.misc;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.Hsrmod;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class AftertastePower extends BuffPower implements PreElementalDamageSubscriber {
    public static final String POWER_ID = Hsrmod.makePath(AftertastePower.class.getSimpleName());
    
    AbstractCard card;
    
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
        SubscriptionManager.subscribe(this, true);
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
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        this.card = null;
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (action.info != null 
                    && action.info.card != null 
                    && action.info.card.hasTag(FOLLOW_UP) 
                    && action.info.card != card
                    && ModHelper.check(action.target) 
                    && action.target != owner
                    && action.info.owner == owner) {
                flash();
                card = action.info.card;
                // addToTop(new ApplyPowerAction(info.owner, info.owner, new EnergyPower(info.owner, -ENERGY_REQUIRED), -ENERGY_REQUIRED));
                addToBot(new ElementalDamageAction(action.target, new ElementalDamageInfo(owner, amount, DamageInfo.DamageType.NORMAL,
                        GeneralUtil.getRandomEnumValue(ElementType.class), 1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(owner, owner, this, 1));
            }
        }
        return dmg;
    }
}
