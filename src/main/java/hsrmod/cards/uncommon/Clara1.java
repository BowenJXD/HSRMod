package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.subscribers.OnPlayerDamagedSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Clara1 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Clara1.class.getSimpleName();

    boolean canBeUsed = false;

    public Clara1() {
        super(ID);
        tags.add(FOLLOW_UP);
        selfRetain = true;
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
        int count = p.exhaustPile.group.size();
        addToBot(new GainBlockAction(p, p, count + block));
    }

    @Override
    public void atTurnStartPreDraw() {
        if (canBeUsed) {
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int var1, DamageInfo var2) {
        if (AbstractDungeon.player.hand.contains(this)
                && AbstractDungeon.actionManager.turnHasEnded) {
            canBeUsed = true;
        }
        return var1;
    }
}
