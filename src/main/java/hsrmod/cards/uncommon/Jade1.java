package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
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
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Jade1 extends BaseCard implements PostDrawSubscriber {
    public static final String ID = Jade1.class.getSimpleName();
    
    public Jade1() {
        super(ID);
        tags.add(FOLLOW_UP);
        isMultiDamage = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = AbstractDungeon.player.hand.size();
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        addToBot(new LoseHPAction(p, p, 2));
        addToBot(new DrawCardAction(p, magicNumber));

        addToBot(
                new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        );
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        ModHelper.addToBotAbstract(() -> BaseMod.subscribe(this));
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        if (SubscriptionManager.checkSubscriber(this)
                && AbstractDungeon.player.hand.contains(this)
                && !this.followedUp) {
            this.followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
