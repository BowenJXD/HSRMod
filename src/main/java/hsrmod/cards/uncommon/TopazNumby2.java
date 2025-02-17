package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.PreFollowUpSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Comparator;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class TopazNumby2 extends BaseCard implements PreFollowUpSubscriber {
    public static final String ID = TopazNumby2.class.getSimpleName();
    
    public TopazNumby2() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
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
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && followedUp;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (c instanceof BaseCard) {
            BaseCard card = (BaseCard) c;
            if (card.followedUp && !followedUp) {
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }

    @Override
    public AbstractCreature preFollowUpAction(AbstractCard card, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && card == this && upgraded && target == null) {
            target = AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).min(Comparator.comparingInt(m -> m.currentHealth)).orElse(null);
        }
        return target;
    }
}
