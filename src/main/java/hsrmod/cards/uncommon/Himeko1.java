package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Himeko1 extends BaseCard implements PreBreakSubscriber {
    public static final String ID = Himeko1.class.getSimpleName();
    
    public Himeko1() {
        super(ID);
        tags.add(FOLLOW_UP);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new CleaveEffect()));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
            );
        }
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
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) 
                && info.elementType != null 
                && info.owner == AbstractDungeon.player) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
