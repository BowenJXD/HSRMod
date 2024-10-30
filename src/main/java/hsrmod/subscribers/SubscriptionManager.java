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
import java.util.List;

/**
 * Singleton class for managing all the subscribers.
 */
public class SubscriptionManager {
    private static SubscriptionManager instance = null;
    
    List<ISubscriber> toRemove = new ArrayList<>();
    List<PreBreakDamageSubscriber> preBreakDamageSubscribers = new ArrayList<>();
    List<PreToughnessReduceSubscriber> preToughnessReduceSubscribers = new ArrayList<>();
    List<PreDoTDamageSubscriber> preDoTDamageSubscribers = new ArrayList<>();
    List<PreEnergyChangeSubscriber> preEnergyChangeSubscribers = new ArrayList<>();
    List<PostBreakBlockSubscriber> postBreakBlockSubscribers = new ArrayList<>();
    List<PreBlockChangeSubscriber> preBlockGainSubscribers = new ArrayList<>();
    List<ICheckUsableSubscriber> checkUsableSubscribers = new ArrayList<>();
    List<ISetToughnessReductionSubscriber> setToughnessReductionSubscribers = new ArrayList<>();

    SubscriptionManager() {}
    
    public static SubscriptionManager getInstance() {
        if (instance == null) {
            instance = new SubscriptionManager();
        }
        return instance;
    }
    
    public static void subscribe(ISubscriber sub) {
        getInstance().subscribeHelper(sub, false);
    }
    
    public static void subscribe(ISubscriber sub, boolean addToFront) {
        getInstance().subscribeHelper(sub, addToFront);
    }

    public void subscribeHelper(ISubscriber sub, boolean addToFront) {
        if (sub instanceof PreBreakDamageSubscriber
            && !preBreakDamageSubscribers.contains(sub)) {
            if (addToFront) preBreakDamageSubscribers.add(0, (PreBreakDamageSubscriber) sub); 
            else preBreakDamageSubscribers.add((PreBreakDamageSubscriber) sub);
        }
        else if (sub instanceof PreToughnessReduceSubscriber
                && !preToughnessReduceSubscribers.contains(sub)) {
            if (addToFront) preToughnessReduceSubscribers.add(0, (PreToughnessReduceSubscriber) sub);
            else preToughnessReduceSubscribers.add((PreToughnessReduceSubscriber) sub);
        }
        else if (sub instanceof PreDoTDamageSubscriber
                && !preDoTDamageSubscribers.contains(sub)) {
            if (addToFront) preDoTDamageSubscribers.add(0, (PreDoTDamageSubscriber) sub);
            else preDoTDamageSubscribers.add((PreDoTDamageSubscriber) sub);
        }
        else if (sub instanceof PreEnergyChangeSubscriber
                && !preEnergyChangeSubscribers.contains(sub)) {
            if (addToFront) preEnergyChangeSubscribers.add(0, (PreEnergyChangeSubscriber) sub);
            else preEnergyChangeSubscribers.add((PreEnergyChangeSubscriber) sub);
        }
        else if (sub instanceof PostBreakBlockSubscriber
                && !postBreakBlockSubscribers.contains(sub)) {
            if (addToFront) postBreakBlockSubscribers.add(0, (PostBreakBlockSubscriber) sub);
            else postBreakBlockSubscribers.add((PostBreakBlockSubscriber) sub);
        }
        else if (sub instanceof PreBlockChangeSubscriber
                && !preBlockGainSubscribers.contains(sub)) {
            if (addToFront) preBlockGainSubscribers.add(0, (PreBlockChangeSubscriber) sub);
            else preBlockGainSubscribers.add((PreBlockChangeSubscriber) sub);
        }
        else if (sub instanceof ICheckUsableSubscriber
                && !checkUsableSubscribers.contains(sub)) {
            if (addToFront) checkUsableSubscribers.add(0, (ICheckUsableSubscriber) sub);
            else checkUsableSubscribers.add((ICheckUsableSubscriber) sub);
        }
        else if (sub instanceof ISetToughnessReductionSubscriber
                && !setToughnessReductionSubscribers.contains(sub)) {
            if (addToFront) setToughnessReductionSubscribers.add(0, (ISetToughnessReductionSubscriber) sub);
            else setToughnessReductionSubscribers.add((ISetToughnessReductionSubscriber) sub);
        }
    }
    
    public static void unsubscribe(ISubscriber subscriber){
        getInstance().unsubscribeHelper(subscriber);
    }
    
    public void unsubscribeHelper(ISubscriber subscriber) {
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
        else if (subscriber instanceof PreBlockChangeSubscriber) {
            preBlockGainSubscribers.remove(subscriber);
        }
        else if (subscriber instanceof ICheckUsableSubscriber) {
            checkUsableSubscribers.remove(subscriber);
        }
        else if (subscriber instanceof ISetToughnessReductionSubscriber) {
            setToughnessReductionSubscribers.remove(subscriber);
        }
    }
    
    public void unsubscribeLater(ISubscriber subscriber) {
        toRemove.add(subscriber);
    }

    private void unsubscribeLaterHelper(Class<? extends ISubscriber> removalClass) {
        for (ISubscriber sub : toRemove) {
            if (removalClass.isInstance(sub)) {
                unsubscribe(sub);
            }
        }
    }
    
    public float triggerPreBreakDamage(float amount, AbstractCreature target) {
        float result = amount;

        for (PreBreakDamageSubscriber sub : preBreakDamageSubscribers) {
            result = sub.preBreakDamage(amount, target);
        }
        
        unsubscribeLaterHelper(PreBreakDamageSubscriber.class);
        
        return result;
    }
    
    public float triggerPreToughnessReduce(int amount, AbstractCreature target, ElementType elementType) {
        float result = amount;

        for (PreToughnessReduceSubscriber sub : preToughnessReduceSubscribers) {
            result = sub.preToughnessReduce(amount, target, elementType);
        }
        
        unsubscribeLaterHelper(PreToughnessReduceSubscriber.class);
        
        return result;
    }
    
    public float triggerPreDoTDamage(float amount, AbstractCreature target, DoTPower power) {
        float result = amount;

        for (PreDoTDamageSubscriber sub : preDoTDamageSubscribers) {
            result = sub.preDoTDamage(amount, target, power);
        }
        
        unsubscribeLaterHelper(PreDoTDamageSubscriber.class);
        
        return result;
    }
    
    public int triggerPreEnergyChange(int changeAmount) {
        int result = changeAmount;

        for (PreEnergyChangeSubscriber sub : preEnergyChangeSubscribers) {
            result = sub.preEnergyChange(changeAmount);
        }
        
        unsubscribeLaterHelper(PreEnergyChangeSubscriber.class);
        
        return result;
    }
    
    public void triggerPostBreakBlock(AbstractCreature target) {
        for (PostBreakBlockSubscriber sub : postBreakBlockSubscribers) {
            sub.postBreakBlock(target);
        }
        
        unsubscribeLaterHelper(PostBreakBlockSubscriber.class);
    }
    
    public int triggerPreBlockChange(AbstractCreature creature, int amount) {
        int result = amount;
        
        for (PreBlockChangeSubscriber sub : preBlockGainSubscribers) {
            result = sub.preBlockChange(creature, amount);
        }
        
        unsubscribeLaterHelper(PreBlockChangeSubscriber.class);
        
        return result;
    }
    
    public boolean triggerCheckUsable(BaseCard card) {
        boolean result = false;
        
        for (ICheckUsableSubscriber sub : checkUsableSubscribers) {
            result |= sub.checkUsable(card);
        }
        
        unsubscribeLaterHelper(ICheckUsableSubscriber.class);
        
        return result;
    }
    
    public int triggerSetToughnessReduction(AbstractCreature target, int amount) {
        int result = amount;
        
        for (ISetToughnessReductionSubscriber sub : setToughnessReductionSubscribers) {
            result = sub.setToughnessReduction(target, amount);
        }
        
        unsubscribeLaterHelper(ISetToughnessReductionSubscriber.class);
        
        return result;
    }
    
    public static boolean checkSubscriber(BaseCard card) {
        boolean result = AbstractDungeon.player.hand.contains(card)
                || AbstractDungeon.player.drawPile.contains(card)
                || AbstractDungeon.player.discardPile.contains(card)
                || AbstractDungeon.player.exhaustPile.contains(card);
        if (!result && card instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) card);
            getInstance().unsubscribeLater((ISubscriber) card);
        }
        return result;
    }
    
    public static boolean checkSubscriber(AbstractPower power){
        boolean result = (power.owner == AbstractDungeon.player && AbstractDungeon.player.powers.contains(power)) 
                || (AbstractDungeon.getMonsters().monsters.contains(power.owner));
        if (!result && power instanceof ISubscriber) {
            BaseMod.unsubscribeLater((ISubscriber) power);
            getInstance().unsubscribeLater((ISubscriber) power);
        }
        return result;
    }
}
