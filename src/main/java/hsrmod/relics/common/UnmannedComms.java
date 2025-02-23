package hsrmod.relics.common;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.IRunnableSubscriber;
import hsrmod.subscribers.PostRelicDestroySubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class UnmannedComms extends BaseRelic implements PostRelicDestroySubscriber {
    public static final String ID = UnmannedComms.class.getSimpleName();
    
    public UnmannedComms() {
        super(ID);
        SubscriptionManager.subscribe(this);
        setCounter(magicNumber);
    }

    @Override
    public void postRelicDestroy(AbstractRelic relic) {
        if (SubscriptionManager.checkSubscriber(this) && relic != this) {
            if (usedUp) {
                SubscriptionManager.getInstance().unsubscribeLater(this);
                return;
            }
            ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(false, relic));
            ModHelper.addEffectAbstract(() -> {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic.makeCopy());
                reduceCounterAndCheckDestroy();
            });
        }
    }
}
