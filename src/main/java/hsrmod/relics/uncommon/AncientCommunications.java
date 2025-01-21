package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.SequenceTrotter;
import hsrmod.monsters.WarpTrotter;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.INumChangerSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class AncientCommunications extends BaseRelic implements INumChangerSubscriber {
    public static final String ID = AncientCommunications.class.getSimpleName();

    public AncientCommunications() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public float changeNum(float base) {
        if (SubscriptionManager.checkSubscriber(this)) {
            if (usedUp) SubscriptionManager.getInstance().unsubscribeLater(this);
            else return base * 4;
        }
        return base;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (usedUp) return;
        ModHelper.addToBotAbstract(() -> {
            if (AbstractDungeon.getMonsters() != null 
                    && AbstractDungeon.getMonsters().monsters != null
                    && AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m instanceof SequenceTrotter || m instanceof WarpTrotter)) {
                flash();
                usedUp();
            }
        });
    }

    @Override
    public SubscriptionManager.NumChangerType getSubType() {
        return SubscriptionManager.NumChangerType.TROTTER_WEIGHT;
    }
}
