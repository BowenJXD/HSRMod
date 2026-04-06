package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.PostHPUpdateSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Evernight1 extends BaseCard implements PostHPUpdateSubscriber {
    public static final String ID = Evernight1.class.getSimpleName();

    public Evernight1() {
        super(ID);
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
        addToBot(new LoseHPAction(p, p, 1));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        for (int i = magicNumber; i > 0; i--) {
            AbstractOrb orb = AbstractDungeon.player.orbs.get(AbstractDungeon.cardRandomRng.random(AbstractDungeon.player.filledOrbCount()));
            if (orb != null) {
                ModHelper.triggerPassiveTo(orb, m);
            }
        }
    }

    @Override
    public void postHPUpdate(AbstractCreature creature) {
        if (SubscriptionManager.checkSubscriber(this) 
                && creature == AbstractDungeon.player
                && AbstractDungeon.player.discardPile.contains(this)
        ) {
            addToTop(new DiscardToHandAction(this));
        }
    }
}
