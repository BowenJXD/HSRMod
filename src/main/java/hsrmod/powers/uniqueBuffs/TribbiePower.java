package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TribbiePower extends PowerPower implements PreElementalDamageSubscriber {
    public static final String ID = HSRMod.makePath(TribbiePower.class.getSimpleName());
    
    HashSet<AbstractCreature> targets;
    
    public TribbiePower(int amount, boolean upgraded) {
        super(ID, amount, upgraded);
        targets = new HashSet<>();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(this.DESCRIPTIONS[upgraded ? 1 : 0], amount);
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        targets.clear();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (targets.isEmpty()) return;
        AbstractCreature targetWithMaxHp = targets.stream().max((a, b) -> a.currentHealth - b.currentHealth).get();
        int dmg = amount * targets.size();
        if (!upgraded) dmg -= amount;
        if (dmg > 0)
            addToTop(new ElementalDamageAction(targetWithMaxHp, new ElementalDamageInfo(owner, dmg, ElementType.Quantum, 0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.info.card != null) {
            targets.add(action.target);
        }
        return dmg;
    }
}
