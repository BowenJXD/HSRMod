package hsrmod.cardsV2.Destruction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.subscribers.PreBreakDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class TheDahlia2 extends BaseCard implements PreBreakDamageSubscriber {
    public static final String ID = TheDahlia2.class.getSimpleName();
    
    public TheDahlia2() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
            addToBot(new DrawCardAction(p, 1));
        addToBot(new ApplyPowerAction(p, p, new BreakEfficiencyPower(p, 1), 1));
        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                ci -> {
                    if (ci.didBreak || (ci.target.hasPower(BrokenPower.POWER_ID) && upgraded)) {
                        addToTop(new BreakDamageAction(ci.target, new DamageInfo(p, tr), AbstractGameAction.AttackEffect.FIRE));
                    }
                }
        );
        addToBot(new BouncingAction(m, magicNumber, elementalDamageAction, this));
    }

    @Override
    public float preBreakDamage(float amount, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
        return amount;
    }
}
