package hsrmod.subscribers;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.DoTPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton class for managing all the subscribers.
 */
public class SubscribeManager {
    private static SubscribeManager instance = null;
    
    List<ISubscriber> toRemove = new ArrayList<>();
    List<PreBreakDamageSubscriber> preBreakDamageSubscribers = new ArrayList<>();
    List<PreToughnessReduceSubscriber> preToughnessReduceSubscribers = new ArrayList<>();
    List<PreDoTDamageSubscriber> preDoTDamageSubscribers = new ArrayList<>();
    List<PreEnergyChangeSubscriber> preEnergyChangeSubscribers = new ArrayList<>();
    List<PostBreakBlockSubscriber> postBreakBlockSubscribers = new ArrayList<>();

    SubscribeManager() {}
    
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
        else if (sub instanceof PreEnergyChangeSubscriber
                && !preEnergyChangeSubscribers.contains(sub)) {
            preEnergyChangeSubscribers.add((PreEnergyChangeSubscriber) sub);
        }
        else if (sub instanceof PostBreakBlockSubscriber
                && !postBreakBlockSubscribers.contains(sub)) {
            postBreakBlockSubscribers.add((PostBreakBlockSubscriber) sub);
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
        else if (subscriber instanceof PreEnergyChangeSubscriber) {
            preEnergyChangeSubscribers.remove(subscriber);
        }
        else if (subscriber instanceof PostBreakBlockSubscriber) {
            postBreakBlockSubscribers.remove(subscriber);
        }
    }
    
    public void unsubscribeLater(ISubscriber subscriber) {
        toRemove.add(subscriber);
    }

    private void unsubscribeLaterHelper(Class<? extends ISubscriber> removalClass) {
        Iterator var1 = toRemove.iterator();

        while(var1.hasNext()) {
            ISubscriber sub = (ISubscriber)var1.next();
            if (removalClass.isInstance(sub)) {
                unsubscribe(sub);
            }
        }
    }
    
    public float triggerPreBreakDamage(float amount, AbstractCreature target) {
        float result = amount;
        Iterator<PreBreakDamageSubscriber> var3 = preBreakDamageSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreBreakDamageSubscriber sub = var3.next();
            result = sub.preBreakDamage(amount, target);
        }
        
        unsubscribeLaterHelper(PreBreakDamageSubscriber.class);
        
        return result;
    }
    
    public float triggerPreToughnessReduce(int amount, AbstractCreature target, ElementType elementType) {
        float result = amount;
        Iterator<PreToughnessReduceSubscriber> var3 = preToughnessReduceSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreToughnessReduceSubscriber sub = var3.next();
            sub.preToughnessReduce(amount, target, elementType);
        }
        
        unsubscribeLaterHelper(PreToughnessReduceSubscriber.class);
        
        return result;
    }
    
    public float triggerPreDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        float result = amount;
        Iterator<PreDoTDamageSubscriber> var3 = preDoTDamageSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreDoTDamageSubscriber sub = var3.next();
            result = sub.preDoTDamage(amount, target, power);
        }
        
        unsubscribeLaterHelper(PreDoTDamageSubscriber.class);
        
        return result;
    }
    
    public int triggerPreEnergyChange(int changeAmount) {
        int result = changeAmount;
        Iterator<PreEnergyChangeSubscriber> var3 = preEnergyChangeSubscribers.iterator();
        
        while (var3.hasNext()) {
            PreEnergyChangeSubscriber sub = var3.next();
            result = sub.receivePreEnergyChange(changeAmount);
        }
        
        unsubscribeLaterHelper(PreEnergyChangeSubscriber.class);
        
        return result;
    }
    
    public void triggerPostBreakBlock(AbstractCreature target) {
        Iterator<PostBreakBlockSubscriber> var3 = postBreakBlockSubscribers.iterator();
        
        while (var3.hasNext()) {
            PostBreakBlockSubscriber sub = var3.next();
            sub.receivePostBreakBlock(target);
        }
        
        unsubscribeLaterHelper(PostBreakBlockSubscriber.class);
    }
    
    public static boolean checkSubscriber(BaseCard card) {
        boolean result = AbstractDungeon.player.hand.contains(card)
                || AbstractDungeon.player.drawPile.contains(card)
                || AbstractDungeon.player.discardPile.contains(card)
                || AbstractDungeon.player.exhaustPile.contains(card);
        if (!result && card instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) card);
            instance.unsubscribeLater((ISubscriber) card);
        }
        return result;
    }
    
    public static boolean checkSubscriber(AbstractPower power){
        boolean result = AbstractDungeon.player.powers.contains(power);
        if (!result && power instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) power);
            instance.unsubscribeLater((ISubscriber) power);
        }
        return result;
    }
}
