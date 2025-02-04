package hsrmod.cardsV2.TheHunt;

import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.monsters.BaseMonster;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Feixiao2 extends BaseCard implements OnCardUseSubscriber {
    public static final String ID = Feixiao2.class.getSimpleName();
    
    int costCache;
    boolean subscribed = false;
    
    public Feixiao2() {
        super(ID);
        costCache = cost;
        BaseMod.subscribe(this);
    }/*

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (!subscribed) {
            BaseMod.subscribe(this);
            subscribed = true;
        }
    }*/

    @Override
    public void updateCost(int amt) {
        super.updateCost(amt);
        followUp();
    }

    @Override
    public void modifyCostForCombat(int amt) {
        super.modifyCostForCombat(amt);
        followUp();
    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);
        followUp();
    }

    void followUp() {
        if (!followedUp && costForTurn == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Feixiao"));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        ModHelper.addToBotAbstract(() -> updateCost(costCache - cost));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (AbstractDungeon.player.hand.contains(this) && !followedUp && c.type == CardType.ATTACK) {
            updateCost(-1);
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (abstractCard != this 
                && SubscriptionManager.checkSubscriber(this) 
                && abstractCard.type == CardType.ATTACK) {
            baseDamage++;
        }
    }
}
