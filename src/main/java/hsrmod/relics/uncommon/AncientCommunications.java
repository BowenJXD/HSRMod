package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.Bonus.SequenceTrotter;
import hsrmod.monsters.Bonus.WarpTrotter;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.INumChangerSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.function.Predicate;

public class AncientCommunications extends BaseRelic implements INumChangerSubscriber {
    public static final String ID = AncientCommunications.class.getSimpleName();

    public AncientCommunications() {
        super(ID);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (usedUp) return;
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                if (AbstractDungeon.getMonsters() != null
                        && AbstractDungeon.getMonsters().monsters != null) {
                    boolean b = false;
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (m instanceof SequenceTrotter || m instanceof WarpTrotter) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        AncientCommunications.this.flash();
                        AncientCommunications.this.destroy();
                    }
                }
            }
        });
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
    public SubscriptionManager.NumChangerType getSubType() {
        return SubscriptionManager.NumChangerType.TROTTER_WEIGHT;
    }
}
