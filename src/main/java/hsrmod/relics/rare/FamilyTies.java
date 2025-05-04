package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.patches.RelicTagField;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.PostRelicDestroySubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class FamilyTies extends BaseRelic implements PostRelicDestroySubscriber {
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
        ModHelper.addEffectAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                RelicEventHelper.gainRelics(1, new Predicate<AbstractRelic>() {
                    @Override
                    public boolean test(AbstractRelic r) {
                        return RelicTagField.destructible.get(r);
                    }
                });
                FamilyTies.this.updateCounter();
            }
        });
    }
    
    void updateCounter() {
        int sum = 0;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            int i = r.counter == -2 ? 1 : 0;
            sum += i;
        }
        setCounter(sum);
    }

    @Override
    public void postRelicDestroy(AbstractRelic relic) {
        if (SubscriptionManager.checkSubscriber(this)) {
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    RelicEventHelper.gainRelics(1, new Predicate<AbstractRelic>() {
                        @Override
                        public boolean test(AbstractRelic r) {
                            return RelicTagField.destructible.get(r);
                        }
                    });
                }
            });
            updateCounter();
        }
    }
}
