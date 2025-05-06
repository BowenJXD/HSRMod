package androidTestMod.powers.uniqueBuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.powers.BuffPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;
import androidTestMod.subscribers.PreBlockChangeSubscriber;
import androidTestMod.subscribers.SubscriptionManager;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;

public class DivinityPower extends BuffPower implements PreBlockChangeSubscriber, OnReceivePowerPower {
    public static final String POWER_ID = AndroidTestMod.makePath(DivinityPower.class.getSimpleName());

    private float loseMultiplier = 1 / 4f;
    boolean triggered = false;
    int cachedTurn = 0;
    
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
        SubscriptionManager.subscribe(this, true);
        cachedTurn = GameActionManager.turn;
    }

    @Override
    public void onRemove() {
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public int preBlockChange(AbstractCreature creature, int blockAmount) {
        if (SubscriptionManager.checkSubscriber(this) 
                && creature == owner) {
            if (blockAmount > 0) {
                triggered = true;
                return blockAmount + amount;
            } else if (blockAmount < 0 && GameActionManager.turn > cachedTurn) {
                cachedTurn = GameActionManager.turn;
                return Math.round(blockAmount * loseMultiplier);
            }
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
