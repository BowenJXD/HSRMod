package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.LockToughnessAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

public class ResonatePower extends StatePower implements PreElementalDamageSubscriber, PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ResonatePower.class.getSimpleName());
    
    AbstractCard card;
    
    public ResonatePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }
    
    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
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
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        this.card = null;
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.target.hasPower(POWER_ID)
                && action.info.type == DamageInfo.DamageType.NORMAL
                && action.info.card != null
                && action.info.card != card) {
            card = action.info.card;
            if (action.target != owner) {
                AbstractGameAction copy = new ElementalDamageAction(owner, new ElementalDamageInfo(action.info.card), action.attackEffect);
                addToTop(copy);
            } else {
                action.info.tr += this.amount;
            }
        }
        return dmg;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            AbstractMonster monster = (AbstractMonster) owner;
            if (monster != null) {
                if (monster.type == AbstractMonster.EnemyType.NORMAL)
                    addToBot(new LockToughnessAction(owner, owner));
                addToBot(new RollMoveAction(monster));
                ModHelper.addToBotAbstract(monster::createIntent);
            }
        }
    }
}
