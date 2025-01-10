package hsrmod.subscribers;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.DoTPower;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for managing all the subscribers.
 */
public class SubscriptionManager {
    private static SubscriptionManager instance = null;

    List<IHSRSubscriber> toRemove = new ArrayList<>();
    List<PreElementalDamageSubscriber> preElementalDamageSubscribers = new ArrayList<>();
    List<PreBreakDamageSubscriber> preBreakDamageSubscribers = new ArrayList<>();
    List<PreToughnessReduceSubscriber> preToughnessReduceSubscribers = new ArrayList<>();
    List<PreBreakSubscriber> preBreakSubscribers = new ArrayList<>();
    List<PreDoTDamageSubscriber> preDoTDamageSubscribers = new ArrayList<>();
    List<PreEnergyChangeSubscriber> preEnergyChangeSubscribers = new ArrayList<>();
    List<PostBreakBlockSubscriber> postBreakBlockSubscribers = new ArrayList<>();
    List<PreBlockChangeSubscriber> preBlockGainSubscribers = new ArrayList<>();
    List<ICheckUsableSubscriber> checkUsableSubscribers = new ArrayList<>();
    List<ISetToughnessReductionSubscriber> setToughnessReductionSubscribers = new ArrayList<>();
    List<PostUpgradeSubscriber> postUpgradeSubscribers = new ArrayList<>();

    SubscriptionManager() {}

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            instance = new SubscriptionManager();
        }
        return instance;
    }
    
    public static void subscribe(IHSRSubscriber sub) {
        getInstance().subscribeHelper(sub, false);
    }

    public static void subscribe(IHSRSubscriber sub, boolean addToFront) {
        getInstance().subscribeHelper(sub, addToFront);
    }

    public void subscribeHelper(ISubscriber sub, boolean addToFront) {
        if (sub instanceof PreElementalDamageSubscriber
                && !preElementalDamageSubscribers.contains(sub)) {
            if (addToFront) preElementalDamageSubscribers.add(0, (PreElementalDamageSubscriber) sub);
            else preElementalDamageSubscribers.add((PreElementalDamageSubscriber) sub);
        } if (sub instanceof PreBreakDamageSubscriber
                && !preBreakDamageSubscribers.contains(sub)) {
            if (addToFront) preBreakDamageSubscribers.add(0, (PreBreakDamageSubscriber) sub);
            else preBreakDamageSubscribers.add((PreBreakDamageSubscriber) sub);
        } if (sub instanceof PreToughnessReduceSubscriber
                && !preToughnessReduceSubscribers.contains(sub)) {
            if (addToFront) preToughnessReduceSubscribers.add(0, (PreToughnessReduceSubscriber) sub);
            else preToughnessReduceSubscribers.add((PreToughnessReduceSubscriber) sub);
        } if (sub instanceof PreBreakSubscriber
                && !preBreakSubscribers.contains(sub)) {
            if (addToFront) preBreakSubscribers.add(0, (PreBreakSubscriber) sub);
            else preBreakSubscribers.add((PreBreakSubscriber) sub);
        } if (sub instanceof PreDoTDamageSubscriber
                && !preDoTDamageSubscribers.contains(sub)) {
            if (addToFront) preDoTDamageSubscribers.add(0, (PreDoTDamageSubscriber) sub);
            else preDoTDamageSubscribers.add((PreDoTDamageSubscriber) sub);
        } if (sub instanceof PreEnergyChangeSubscriber
                && !preEnergyChangeSubscribers.contains(sub)) {
            if (addToFront) preEnergyChangeSubscribers.add(0, (PreEnergyChangeSubscriber) sub);
            else preEnergyChangeSubscribers.add((PreEnergyChangeSubscriber) sub);
        } if (sub instanceof PostBreakBlockSubscriber
                && !postBreakBlockSubscribers.contains(sub)) {
            if (addToFront) postBreakBlockSubscribers.add(0, (PostBreakBlockSubscriber) sub);
            else postBreakBlockSubscribers.add((PostBreakBlockSubscriber) sub);
        } if (sub instanceof PreBlockChangeSubscriber
                && !preBlockGainSubscribers.contains(sub)) {
            if (addToFront) preBlockGainSubscribers.add(0, (PreBlockChangeSubscriber) sub);
            else preBlockGainSubscribers.add((PreBlockChangeSubscriber) sub);
        } if (sub instanceof ICheckUsableSubscriber
                && !checkUsableSubscribers.contains(sub)) {
            if (addToFront) checkUsableSubscribers.add(0, (ICheckUsableSubscriber) sub);
            else checkUsableSubscribers.add((ICheckUsableSubscriber) sub);
        } if (sub instanceof ISetToughnessReductionSubscriber
                && !setToughnessReductionSubscribers.contains(sub)) {
            if (addToFront) setToughnessReductionSubscribers.add(0, (ISetToughnessReductionSubscriber) sub);
            else setToughnessReductionSubscribers.add((ISetToughnessReductionSubscriber) sub);
        } if (sub instanceof PostUpgradeSubscriber
                && !postUpgradeSubscribers.contains(sub)) {
            if (addToFront) postUpgradeSubscribers.add(0, (PostUpgradeSubscriber) sub);
            else postUpgradeSubscribers.add((PostUpgradeSubscriber) sub);
        }
    }

    public static void unsubscribe(ISubscriber sub) {
        BaseMod.unsubscribe(sub);
    }
    
    public static void unsubscribe(IHSRSubscriber sub) {
        getInstance().unsubscribeHelper(sub);
    }

    public void unsubscribeHelper(ISubscriber sub) {
        if (sub instanceof PreElementalDamageSubscriber) {
            preElementalDamageSubscribers.remove(sub);
        } if (sub instanceof PreBreakDamageSubscriber) {
            preBreakDamageSubscribers.remove(sub);
        } if (sub instanceof PreToughnessReduceSubscriber) {
            preToughnessReduceSubscribers.remove(sub);
        } if (sub instanceof PreBreakSubscriber) {
            preBreakSubscribers.remove(sub);
        } if (sub instanceof PreDoTDamageSubscriber) {
            preDoTDamageSubscribers.remove(sub);
        } if (sub instanceof PreEnergyChangeSubscriber) {
            preEnergyChangeSubscribers.remove(sub);
        } if (sub instanceof PostBreakBlockSubscriber) {
            postBreakBlockSubscribers.remove(sub);
        } if (sub instanceof PreBlockChangeSubscriber) {
            preBlockGainSubscribers.remove(sub);
        } if (sub instanceof ICheckUsableSubscriber) {
            checkUsableSubscribers.remove(sub);
        } if (sub instanceof ISetToughnessReductionSubscriber) {
            setToughnessReductionSubscribers.remove(sub);
        } if (sub instanceof PostUpgradeSubscriber) {
            postUpgradeSubscribers.remove(sub);
        }
    }

    public void unsubscribeLater(IHSRSubscriber sub) {
        toRemove.add(sub);
    }

    private void unsubscribeLaterHelper(Class<? extends ISubscriber> removalClass) {
        for (IHSRSubscriber sub : toRemove) {
            if (removalClass.isInstance(sub)) {
                unsubscribe(sub);
            }
        }
        toRemove.clear();
    }

    public float triggerPreElementalDamage(ElementalDamageAction action) {
        float result = action.info.output;

        for (PreElementalDamageSubscriber sub : preElementalDamageSubscribers) {
            result = sub.preElementalDamage(action, result);
        }

        unsubscribeLaterHelper(PreElementalDamageSubscriber.class);

        return result;
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
            result = sub.preToughnessReduce(result, target, elementType);
        }

        unsubscribeLaterHelper(PreToughnessReduceSubscriber.class);

        return result;
    }

    public void triggerPreBreak(ElementalDamageInfo info, AbstractCreature target) {
        for (PreBreakSubscriber sub : preBreakSubscribers) {
            sub.preBreak(info, target);
        }

        unsubscribeLaterHelper(PreBreakSubscriber.class);
    }

    public float triggerPreDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        float result = info.output;

        for (PreDoTDamageSubscriber sub : preDoTDamageSubscribers) {
            result = sub.preDoTDamage(info, target, power);
        }

        unsubscribeLaterHelper(PreDoTDamageSubscriber.class);

        return result;
    }

    public int triggerPreEnergyChange(int changeAmount) {
        int result = changeAmount;

        for (PreEnergyChangeSubscriber sub : preEnergyChangeSubscribers) {
            result = sub.preEnergyChange(result);
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
            result = sub.preBlockChange(creature, result);
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
            result = sub.setToughnessReduction(target, result);
        }

        unsubscribeLaterHelper(ISetToughnessReductionSubscriber.class);

        return result;
    }
    
    public void triggerPostUpgrade(AbstractCard card) {
        for (PostUpgradeSubscriber sub : postUpgradeSubscribers) {
            sub.postUpgrade(card);
        }

        unsubscribeLaterHelper(PostUpgradeSubscriber.class);
    }

    public static boolean checkSubscriber(BaseCard card) {
        boolean result = AbstractDungeon.player.hand.contains(card)
                || AbstractDungeon.player.drawPile.contains(card)
                || AbstractDungeon.player.discardPile.contains(card)
                || AbstractDungeon.player.exhaustPile.contains(card);
        if (!result) {
            if (card instanceof IHSRSubscriber) getInstance().unsubscribeLater((IHSRSubscriber) card);
            if (card instanceof ISubscriber) BaseMod.unsubscribeLater((ISubscriber) card);
        }
        return result;
    }

    public static boolean checkSubscriber(AbstractPower power) {
        boolean result = (
                power.owner == AbstractDungeon.player
                        && AbstractDungeon.player.powers.contains(power))
                || (
                AbstractDungeon.getMonsters() != null
                        && AbstractDungeon.getMonsters().monsters != null
                        && AbstractDungeon.getMonsters().monsters.contains(power.owner)
        );
        if (!result) {
            if (power instanceof IHSRSubscriber) getInstance().unsubscribeLater((IHSRSubscriber) power);
            if (power instanceof ISubscriber) BaseMod.unsubscribeLater((ISubscriber) power);
        }
        return result;
    }
    
    public static boolean checkSubscriber(AbstractMonster monster) {
        boolean result = AbstractDungeon.getMonsters().monsters.contains(monster);
        if (!result) {
            if (monster instanceof IHSRSubscriber) getInstance().unsubscribeLater((IHSRSubscriber) monster);
            if (monster instanceof ISubscriber) BaseMod.unsubscribeLater((ISubscriber) monster);
        }
        return result;
    }
    
    public static boolean checkSubscriber(AbstractRelic relic) {
        boolean result = AbstractDungeon.player.relics.contains(relic);
        if (!result) {
            if (relic instanceof IHSRSubscriber) getInstance().unsubscribeLater((IHSRSubscriber) relic);
            if (relic instanceof ISubscriber) BaseMod.unsubscribeLater((ISubscriber) relic);
        }
        return result;
    }
}
