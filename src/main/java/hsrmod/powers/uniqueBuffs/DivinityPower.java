package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PreBlockChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class DivinityPower extends PowerPower implements OnPlayerLoseBlockSubscriber, PreBlockChangeSubscriber, OnReceivePowerPower {
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
        SubscriptionManager.subscribe(this, true);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (!SubscriptionManager.checkSubscriber(this)) return i;
        return Math.round(i * loseMultiplier);
    }

    @Override
    public int preBlockChange(AbstractCreature creature, int blockAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && creature == owner 
                && blockAmount > 0) {
            return blockAmount + blockAdd;
        }
        return blockAmount;
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        super.onGainedBlock(blockAmount);
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof DexterityPower || abstractPower instanceof FrailPower) {
            flash();
            return false;
        }
        return true;
    }
}
