package hsrmod.cards.uncommon;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.PreFollowUpSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TopazNumby2 extends BaseCard implements PreFollowUpSubscriber {
    public static final String ID = TopazNumby2.class.getSimpleName();

    int count = 0;
    int unlockCount = 0;
    int cachedTurn = -1;

    public TopazNumby2() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        SubscriptionManager.subscribe(this);
        if (cachedTurn == -1 || GameActionManager.turn != cachedTurn) {
            cachedTurn = GameActionManager.turn;
            unlockCount = 0;
        }
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c instanceof BaseCard) {
            BaseCard baseCard = (BaseCard) c;
            if (baseCard.followedUp 
                    && !followedUp 
                    && !(c instanceof TopazNumby2)
                    && AbstractDungeon.player.hand.findCardById(cardID) == this) {
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        super.triggerOnCardPlayed(cardPlayed);
        if (cardPlayed instanceof BaseCard && cardPlayed.hasTag(FOLLOW_UP) && ((BaseCard)cardPlayed).followedUp) {
            AbstractCard card = null;
            List<AbstractCard> discardPile = AbstractDungeon.player.discardPile.group;
            for (int i = discardPile.size() - 1; i >= 0; i--) {
                if (discardPile.get(i).cardID.equals(cardID)) {
                    card = discardPile.get(i);
                    break;
                }
            }
            if (card != null) { 
                count++;
                if (count == 2) {
                    count = 0;
                    addToBot(new DiscardToHandAction(card));
                }
            }
        }
    }

    @Override
    public AbstractCreature preFollowUpAction(AbstractCard card, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && card == this && upgraded && target == null) {
            boolean seen = false;
            AbstractMonster best = null;

            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (ModHelper.check(monster)) {
                    if (!seen || monster.currentHealth < best.currentHealth) {
                        seen = true;
                        best = monster;
                    }
                }
            }
            target = seen ? best : null;
        }
        return target;
    }
}
