package hsrmod.cardsV2.Curse;

import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.subscribers.ICheckUsableSubscriber;
import hsrmod.subscribers.SubscriptionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Entangle extends BaseCard implements PostDrawSubscriber, ICheckUsableSubscriber {
    public static final String ID = Entangle.class.getSimpleName();
    
    int entangleCount = 2;
    float particleTimer = 0;
    List<AbstractCard> entangledCards;
    
    public Entangle() {
        super(ID, CardColor.COLORLESS);
        exhaust = true;
        tags.add(CustomEnums.ENTANGLE);
        particleTimer = 0.0F;
        entangledCards = new ArrayList<>();
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
        SubscriptionManager.subscribe(this);
        if (entangledCards.isEmpty()) {
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                if (!abstractCard.hasTag(CustomEnums.ENTANGLE)) {
                    tmp.add(abstractCard);
                }
            }
            if (!tmp.isEmpty()) {
                Collections.shuffle(tmp, new Random(AbstractDungeon.cardRandomRng.randomLong()));
                long limit = entangleCount;
                for (AbstractCard c : tmp) {
                    if (limit-- == 0) break;
                    c.tags.add(CustomEnums.ENTANGLE);
                    entangledCards.add(c);
                }
            }
        }
        AbstractDungeon.player.hand.glowCheck();
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
        SubscriptionManager.unsubscribe(this);
        AbstractDungeon.player.hand.glowCheck();
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard c : entangledCards) {
            Entangle.this.addToBot(new FollowUpAction(c));
            c.tags.remove(CustomEnums.ENTANGLE);
        }
        entangledCards.clear();
    }

    @Override
    public void update() {
        super.update();
        if (!entangledCards.isEmpty() && inHand) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.33F;
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (entangledCards.contains(c)) {
                        AbstractDungeon.topLevelEffectsQueue.add(new SmallLaserEffect(current_x, current_y, c.current_x, c.current_y));
                    }
                }
            }
        }
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        if (SubscriptionManager.checkSubscriber(this) 
                && !abstractCard.hasTag(CustomEnums.ENTANGLE) 
                && entangledCards.size() < entangleCount) {
            abstractCard.tags.add(CustomEnums.ENTANGLE);
            entangledCards.add(abstractCard);
        }
    }

    @Override
    public boolean checkUsable(AbstractCard card) {
        if (SubscriptionManager.checkSubscriber(this) 
                && inHand
                && entangledCards.contains(card)) {
            card.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }
}
