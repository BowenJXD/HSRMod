package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.BasePower;
import hsrmod.powers.misc.DewDropPower;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.subscribers.PrePowerTriggerSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class HerbPlucker extends BaseCard implements PrePowerTriggerSubscriber {
    public static final String ID = HerbPlucker.class.getSimpleName();
    
    public HerbPlucker(){
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
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        if (!upgraded) return;
        addToBot(new TriggerPowerAction(AbstractDungeon.player.getPower(NecrosisPower.POWER_ID)));
        addToBot(new TriggerPowerAction(AbstractDungeon.player.getPower(DewDropPower.POWER_ID)));
    }

    @Override
    public void prePowerTrigger(BasePower power) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (power instanceof DewDropPower) {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergizedPower(AbstractDungeon.player, 1)));
            }
            if (power instanceof NecrosisPower) {
                addToBot(new DrawCardAction(1));
            }
        }
    }

    @Override
    protected boolean checkSpawnable() {
        if (AbstractDungeon.player.masterDeck.findCardById(cardID) != null) return false;
        return super.checkSpawnable();
    }
}
