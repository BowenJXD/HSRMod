package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Herta1 extends BaseCard implements PreElementalDamageSubscriber {
    public static final String ID = Herta1.class.getSimpleName();

    public Herta1() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        this.isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        );
    }

    @Override
    public void onEnterHand() {
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void triggerAtStartOfTurn() {
        SubscriptionManager.unsubscribe(this);
    }
    
    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)
                && inHand
                && !followedUp
                && action.target != null
                && action.target.currentHealth > action.target.maxHealth / 2) {
            ModHelper.addToTopAbstract(() -> {
                if (action.target.currentHealth <= action.target.maxHealth / 2) {
                    followedUp = true;
                    addToTop(new FollowUpAction(this));
                }
            });
        }
        return dmg;
    }
}
