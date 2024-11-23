package hsrmod.relics.boss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.misc.ICanChangeToMulti;
import hsrmod.powers.misc.BrainInAVatPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.PostUpgradeSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class TheWindSoaringValorous extends BaseRelic implements PostUpgradeSubscriber {
    public static final String ID = TheWindSoaringValorous.class.getSimpleName();

    public int chargeAmount = 40;
    
    public TheWindSoaringValorous() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }
    
    @Override
    public void onUnequip() {
        super.onUnequip();
        SubscriptionManager.unsubscribe(this);
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && c instanceof ICanChangeToMulti && c.upgraded)
                .forEach(r -> ((ICanChangeToMulti) r).changeToMulti());
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        super.onUseCard(targetCard, useCardAction);
        if (targetCard.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, chargeAmount), chargeAmount));
        }
    }

    @Override
    public void postUpgrade(AbstractCard card) {
        if (SubscriptionManager.checkSubscriber(this)){
            if (card.hasTag(AbstractCard.CardTags.STARTER_STRIKE) 
                    && card instanceof ICanChangeToMulti
                    && card.target != AbstractCard.CardTarget.ALL_ENEMY) {
                flash();
                ((ICanChangeToMulti) card).changeToMulti();
            }
        }
    }
}
