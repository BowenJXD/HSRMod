package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreBlockChangeSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class DivinityPower extends BuffPower implements OnPlayerLoseBlockSubscriber, PreBlockChangeSubscriber, OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(DivinityPower.class.getSimpleName());

    private float loseMultiplier = 1 / 4f;
    boolean triggered = false;
    
    public DivinityPower(AbstractCreature owner, int amount, float loseMultiplier) {
        super(POWER_ID, owner, amount);
        this.amount = amount;
        this.loseMultiplier = loseMultiplier;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount, (int) (loseMultiplier * 100));
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
            triggered = true;
            return blockAmount + amount;
        }
        return blockAmount;
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
