/*
package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Clara1 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Clara1.class.getSimpleName();

    boolean canBeUsed = false;
    
    public Clara1() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (canBeUsed || followedUp);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        canBeUsed = false;

        ModHelper.getRandomElements(p.exhaustPile.group.stream().filter((c) -> c.hasTag(FOLLOW_UP)).collect(Collectors.toList()), 
                AbstractDungeon.cardRandomRng, magicNumber).forEach((c) -> {
                    addToBot(new FollowUpAction(c));
        });
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atTurnStartPreDraw() {
        if (canBeUsed) {
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (!SubscriptionManager.checkSubscriber(this)
                || !AbstractDungeon.player.hand.contains(this)
                || !AbstractDungeon.actionManager.turnHasEnded) return i;
        canBeUsed = true;
        return i;
    }
}
*/
