package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.IRunnableSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class FamilyTies extends BaseRelic implements IRunnableSubscriber {
    public static final String ID = FamilyTies.class.getSimpleName();

    public FamilyTies() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        updateCounter();
        addToBot(new GainEnergyAction(counter));
        addToBot(new DrawCardAction(counter));
    }

    @Override
    public void onEquip() {
        super.onEquip();
        ModHelper.addEffectAbstract(() -> {
            RelicEventHelper.gainRelics(1, r -> RelicTagField.destructible.get(r));
            updateCounter();
        });
    }
    
    void updateCounter() {
        setCounter(AbstractDungeon.player.relics.stream().mapToInt(r -> r.counter == -2 ? 1 : 0 ).sum());
    }

    @Override
    public void run() {
        if (SubscriptionManager.checkSubscriber(this)) {
            ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(1, r -> RelicTagField.destructible.get(r)));
            updateCounter();
        }
    }

    @Override
    public SubscriptionManager.RunnableType getSubType() {
        return SubscriptionManager.RunnableType.ON_RELIC_DESTROYS;
    }
}
