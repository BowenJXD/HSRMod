package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.PreBlockGainSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class DivinityPower extends PowerPower implements OnPlayerLoseBlockSubscriber, PreBlockGainSubscriber {
    public static final String POWER_ID = HSRMod.makePath(DivinityPower.class.getSimpleName());

    private int blockAdd = 2;
    private float loseMultiplier = 1 / 4f;
    
    public DivinityPower(int blockAdd, float loseMultiplier) {
        super(POWER_ID);
        this.blockAdd = blockAdd;
        this.loseMultiplier = loseMultiplier;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], blockAdd, (int) (loseMultiplier * 100));
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
        SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (!SubscriptionManager.checkSubscriber(this)) return i;
        return Math.round(i * loseMultiplier);
    }

    @Override
    public int preBlockGain(AbstractCreature creature, int blockAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && creature == owner) {
            return blockAmount + blockAdd;
        }
        return blockAmount;
    }
}
