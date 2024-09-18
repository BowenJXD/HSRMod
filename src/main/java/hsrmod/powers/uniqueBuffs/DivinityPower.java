package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.SubscribeManager;

public class DivinityPower extends PowerPower implements OnPlayerLoseBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(DivinityPower.class.getSimpleName());

    private float blockMultiplier = 2;
    private float loseMultiplier = 1 / 4f;
    
    public DivinityPower(float blockMultiplier, float loseMultiplier) {
        super(POWER_ID);
        this.blockMultiplier = blockMultiplier;
        this.loseMultiplier = loseMultiplier;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], (int) (blockMultiplier * 100), (int) (loseMultiplier * 100));
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * (1+blockMultiplier);
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (!SubscribeManager.checkSubscriber(this)) return i;
        return (int) (i * loseMultiplier);
    }
}
