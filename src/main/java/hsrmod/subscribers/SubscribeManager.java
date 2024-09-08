package hsrmod.subscribers;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.DoTPower;

import java.util.*;

/**
 * Singleton class for managing all the subscribers.
 */
public class SubscribeManager {
    private static SubscribeManager instance = null;
    
    List<PreBreakDamageSubscriber> preBreakDamageSubscribers;
    List<PreToughnessReduceSubscriber> preToughnessReduceSubscribers;
    List<PreDoTDamageSubscriber> preDoTDamageSubscribers;

    SubscribeManager() {
        preBreakDamageSubscribers = new ArrayList<>();
        preToughnessReduceSubscribers = new ArrayList<>();
        preDoTDamageSubscribers = new ArrayList<>();
    }
    
    public static SubscribeManager getInstance() {
        if (instance == null) {
            instance = new SubscribeManager();
        }
        return instance;
    }

    public void subscribe(ISubscriber sub) {
        if (sub instanceof PreBreakDamageSubscriber
            && !preBreakDamageSubscribers.contains(sub)) {
            preBreakDamageSubscribers.add((PreBreakDamageSubscriber) sub);
        }
        else if (sub instanceof PreToughnessReduceSubscriber
                && !preToughnessReduceSubscribers.contains(sub)) {
            preToughnessReduceSubscribers.add((PreToughnessReduceSubscriber) sub);
        }
        else if (sub instanceof PreDoTDamageSubscriber
                && !preDoTDamageSubscribers.contains(sub)) {
            preDoTDamageSubscribers.add((PreDoTDamageSubscriber) sub);
        }
    }
    
    public void unsubscribe(ISubscriber subscriber) {
        if (subscriber instanceof PreBreakDamageSubscriber) {
            preBreakDamageSubscribers.remove(subscriber);
        }
        else if (subscriber instanceof PreToughnessReduceSubscriber) {
            preToughnessReduceSubscribers.remove(subscriber);
        }
        else if (subscriber instanceof PreDoTDamageSubscriber) {
            preDoTDamageSubscribers.remove(subscriber);
        }
    }
    
    public float triggerPreBreakDamage(int amount, AbstractCreature target) {
        float result = amount;
        Iterator<PreBreakDamageSubscriber> var3 = preBreakDamageSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreBreakDamageSubscriber sub = var3.next();
            result = sub.preBreakDamage(amount, target);
        }
        return result;
    }
    
    public float triggerPreToughnessReduce(int amount, AbstractCreature target, ElementType elementType) {
        float result = amount;
        Iterator<PreToughnessReduceSubscriber> var3 = preToughnessReduceSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreToughnessReduceSubscriber sub = var3.next();
            sub.preToughnessReduce(amount, target, elementType);
        }
        return result;
    }
    
    public float triggerPreDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        float result = amount;
        Iterator<PreDoTDamageSubscriber> var3 = preDoTDamageSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreDoTDamageSubscriber sub = var3.next();
            result = sub.preDoTDamage(amount, target, power);
        }
        return result;
    }
    
    public static boolean checkSubscriber(BaseCard card) {
        boolean result = AbstractDungeon.player.hand.contains(card)
                || AbstractDungeon.player.drawPile.contains(card)
                || AbstractDungeon.player.discardPile.contains(card)
                || AbstractDungeon.player.exhaustPile.contains(card);
        if (!result && card instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) card);
        }
        return result;
    }
    
    public static boolean checkSubscriber(AbstractPower power){
        boolean result = AbstractDungeon.player.powers.contains(power);
        if (!result && power instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) power);
        }
        return result;
    }
}
