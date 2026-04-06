package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.cards.BaseCard;
import hsrmod.powers.uniqueBuffs.ObsessionPower;
import hsrmod.subscribers.PreOrbPassiveOrEvokeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class TimesMiracle extends BaseCard implements PreOrbPassiveOrEvokeSubscriber {
    public static final String ID = TimesMiracle.class.getSimpleName();
    
    public TimesMiracle() {
        super(ID);
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
        
    }

    @Override
    public int preOrbPassive(AbstractOrb orb, int amountThisTime) {
        if (SubscriptionManager.checkSubscriber(this) && inHand) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ObsessionPower(AbstractDungeon.player, 1), 1));
        }
        return amountThisTime;
    }

    @Override
    public int preOrbEvoke(AbstractOrb orb, int amountThisTime) {
        if (SubscriptionManager.checkSubscriber(this) && inHand && upgraded) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ObsessionPower(AbstractDungeon.player, 1), 1));
        }
        return amountThisTime;
    }
}
