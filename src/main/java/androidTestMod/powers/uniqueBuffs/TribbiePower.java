package androidTestMod.powers.uniqueBuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.modcore.ElementType;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.PowerPower;
import androidTestMod.subscribers.PreElementalDamageSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;
import java.util.Optional;

public class TribbiePower extends PowerPower implements PreElementalDamageSubscriber {
    public static final String ID = AndroidTestMod.makePath(TribbiePower.class.getSimpleName());
    
    HashSet<AbstractCreature> targets;
    int totalDmgForTurn;
    
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
    public void atStartOfTurn() {
        super.atStartOfTurn();
        totalDmgForTurn = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        targets.clear();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (upgraded) {
            HashSet<AbstractCreature> abstractCreatures = targets;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (ModHelper.check(monster)) {
                    abstractCreatures.add(monster);
                }
            }
        }
        if (targets.isEmpty()) return;
        boolean seen = false;
        AbstractCreature best = null;
        for (AbstractCreature target : targets) {
            if (!seen || target.currentHealth > best.currentHealth) {
                seen = true;
                best = target;
            }
        }
        AbstractCreature targetWithMaxHp = (seen ? Optional.of(best) : Optional.<AbstractCreature>empty()).get();
        int dmg = amount * targets.size();
        if (!upgraded) dmg -= amount;
        if (dmg > 0) {
            ElementalDamageInfo info = new ElementalDamageInfo(owner, dmg, ElementType.Quantum, 0);
            info.applyPowers(owner, targetWithMaxHp);
            addToTop(new ElementalDamageAction(targetWithMaxHp, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            totalDmgForTurn += dmg;
        }
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.info.card != null 
                && !upgraded) {
            targets.add(action.target);
        }
        return dmg;
    }
}
