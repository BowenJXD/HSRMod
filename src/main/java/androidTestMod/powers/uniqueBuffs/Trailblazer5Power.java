package androidTestMod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.BreakDamageAction;
import androidTestMod.modcore.ElementType;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.powers.PowerPower;
import androidTestMod.powers.misc.BrokenPower;
import androidTestMod.subscribers.PreToughnessReduceSubscriber;
import androidTestMod.subscribers.SubscriptionManager;

public class Trailblazer5Power extends PowerPower implements PreToughnessReduceSubscriber {
    public static final String POWER_ID = AndroidTestMod.makePath(Trailblazer5Power.class.getSimpleName());

    AbstractCard cardCache;
    
    public Trailblazer5Power() {
        super(POWER_ID);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        cardCache = card;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        cardCache = null;
    }

    @Override
    public float preToughnessReduce(float amount, AbstractCreature target, ElementType elementType) {
        if (!SubscriptionManager.checkSubscriber(this) 
                || cardCache == null 
                || !target.hasPower(BrokenPower.POWER_ID)) return amount;
        flash();
        cardCache = null;
        DamageInfo info = new DamageInfo(owner, (int)amount);
        addToBot(new BreakDamageAction(target, info));
        return amount;
    }
}
